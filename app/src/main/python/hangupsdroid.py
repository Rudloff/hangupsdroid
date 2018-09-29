"""Main module used by HangupsDroid"""

import asyncio
import logging
import sys
import hangups
import janus

class CoroutineQueue:
    """Queue used to start asynchronous task from a synchronous function
    (Usually from a Java method.)
    """
    def __init__(self):
        loop = asyncio.get_event_loop()
        self._queue = janus.Queue(loop=loop)

    def put(self, coro):
        """Add a new task in the queue (synchronously)"""
        assert asyncio.iscoroutine(coro)
        self._queue.sync_q.put(coro)

    async def consume(self):
        """ Starts the queue and waits for new tasks"""
        while True:
            coro = await self._queue.async_q.get()
            assert asyncio.iscoroutine(coro)
            await coro
            self._queue.async_q.task_done()

def get_num_unread(conversation):
    """Return the number of unread messages in the conversation"""
    return len([event for event in conversation.unread_events if
                isinstance(event, hangups.ChatMessageEvent) and
                not conversation.get_user(event.user_id).is_self])

def get_message(events):
    """Return the first chat message from the list"""
    for event in events:
        if isinstance(event, hangups.ChatMessageEvent):
            return event

def get_last_message(conversation):
    """Return the last chat message in this conversation"""
    return get_message(reversed(conversation.events))

def get_first_message(conversation):
    """Return the first chat message in this conversation"""
    return get_message(conversation.events)

def get_chat_messages(conversation_events):
    """Return all events in the list that are chat messages"""
    messages = []

    for event in conversation_events:
        if isinstance(event, hangups.ChatMessageEvent):
            messages.append(event)

    return messages

def get_other_user(users):
    """Get the first non-self user from the list
    (It only returns one user so it is not useful for group conversations.)
    """
    for user in users:
        if not user.is_self:
            return user

def get_self_user(users):
    """Get the first self user from the list
    (There should never be more than one.)
    """
    for user in users:
        if user.is_self:
            return user

def get_from_array(array, index):
    """Proxy function to get an element from an array
    We need this because Chaqopy does not provide a way to use the [] syntax.
    """
    return array[index]

def get_attachment(event):
    """Return the first attachment included in an event (if any)"""
    if len(event.attachments) > 0:
        return event.attachments[0]

def event_received(activity, event):
    """Call the activity callback if the event is a chat message"""
    if isinstance(event, hangups.ChatMessageEvent):
        activity.onChatMessageEvent(event)

def connected(activity):
    """Proxy function to call the activity callback
    We need this because Python does not think Java methods are callables.
    """
    activity.onConnected()

class App():
    """Main class used by HangupsDroid
    Used to manage the hangups client.
    """

    def __init__(self):
        self.client = None
        self.coroutine_queue = None
        self.conversation_list = None
        self.user_list = None

    def get_user(self, user_id):
        """Return the user with the specified ID"""
        return self.user_list.get_user(user_id)

    def get_conversation(self, conversation_id):
        """Return the conversation with the specified ID"""
        return self.conversation_list.get(conversation_id)

    async def get_conversations(self, activity):
        """Fetch all conversations and pass them to the actibity callback"""
        self.user_list, self.conversation_list = (
            await hangups.build_user_conversation_list(self.client)
        )

        self.conversation_list.on_event.add_observer(lambda event: event_received(activity, event))

        conversations = self.conversation_list.get_all()
        conversations.reverse()
        activity.onNewConversations(conversations)

    def add_conversations(self, activity):
        """Tell the coroutine queue to fetch conversations"""
        self.coroutine_queue.put(self.get_conversations(activity))

    async def get_older_messages(self, activity, conversation, last_message_id=None):
        """Fetch older messages in this conversation and call the activity callback"""
        conversation_events = await conversation.get_events(last_message_id)

        activity.onNewMessages(get_chat_messages(conversation_events))

    def add_conversation_observer(self, activity, conversation_id):
        """Add an event observer for this conversation"""
        self.get_conversation(conversation_id).on_event.add_observer(
            lambda event: event_received(activity, event)
        )

    def add_messages(self, activity, conversation_id, last_message_id=None):
        """Tell the coroutine queue to fetch new messages for this conversation"""
        self.coroutine_queue.put(
            self.get_older_messages(
                activity,
                self.get_conversation(conversation_id),
                last_message_id
            )
        )

    def set_read(self, conversation_id):
        """Tell the coroutine queue to fetch new messages for this conversation
        Also adds an event observer on the conversation.
        """
        conversation = self.get_conversation(conversation_id)
        self.coroutine_queue.put(conversation.update_read_timestamp())

    async def get_auth(self, activity, prompt, cache):
        """Get auth cookies and pass them to the activity callback"""
        cookies = hangups.get_auth(prompt, cache)
        activity.onAuth(cookies)

    async def connect(self, activity, cookies):
        """Connect to hangouts and start the coroutine queue
        This keeps running as long as hangups is connected.
        """
        self.client = hangups.Client(cookies)

        # Redirect logging to logcat.
        logger = logging.getLogger()
        logger.setLevel(logging.DEBUG)
        stream_handler = logging.StreamHandler(sys.stdout)
        stream_handler.setLevel(logging.INFO)
        stream_handler.setFormatter(
            logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
        )
        logger.addHandler(stream_handler)

        # The CoroutineQueue needs to be created inside the AsyncTask.
        self.coroutine_queue = CoroutineQueue()

        self.client.on_connect.add_observer(lambda: connected(activity))
        coros = [self.client.connect(), self.coroutine_queue.consume()]
        await asyncio.gather(*coros)
