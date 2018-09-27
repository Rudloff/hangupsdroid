from java import jclass

import asyncio
import hangups
import janus

class CoroutineQueue:
    def __init__(self):
        loop = asyncio.get_event_loop()
        self._queue = janus.Queue(loop=loop)

    def put(self, coro):
        assert asyncio.iscoroutine(coro)
        self._queue.sync_q.put(coro)

    async def consume(self):
        while True:
            coro = await self._queue.async_q.get()
            assert asyncio.iscoroutine(coro)
            await coro
            self._queue.async_q.task_done()

def getNumUnread(conversation):
    return len([event for event in conversation.unread_events if
        isinstance(event, hangups.ChatMessageEvent) and
        not conversation.get_user(event.user_id).is_self])

def getLastMessage(conversation):
    for event in conversation.events:
        if isinstance(event, hangups.ChatMessageEvent):
            return event

def getOtherUser(users):
    for user in users:
        if not user.is_self:
            return user

def getSelfUser(users):
    for user in users:
        if user.is_self:
            return user

class App():
    def connected(self, activity):
        activity.onConnected()

    def getUser(self, id):
        return self.user_list.get_user(id)

    def getConversation(self, id):
        return self.conversation_list.get(id)

    async def getConversations(self, activity):
        self.user_list, self.conversation_list = (
            await hangups.build_user_conversation_list(self.client)
        )

        conversations = self.conversation_list.get_all()
        conversations.reverse()
        activity.addConversations(conversations)

    def addConversations(self, activity):
        self.coroutine_queue.put(self.getConversations(activity))

    async def getMessages(self, activity, conversation):
        conversation_events = await conversation.get_events()
        messages = []

        for event in conversation.events:
            if isinstance(event, hangups.ChatMessageEvent):
                messages.append(event)

        activity.addMessages(messages)

    def addMessages(self, activity, conversationId):
        self.coroutine_queue.put(self.getMessages(activity, self.getConversation(conversationId)))

    async def getAuth(self, activity, prompt, cache):
        cookies = hangups.get_auth(prompt, cache)
        activity.onAuth(cookies)

    async def connect(self, activity, cookies):
        self.client = hangups.Client(cookies)

        # The CoroutineQueue needs to be created inside the AsyncTask.
        self.coroutine_queue = CoroutineQueue()

        self.client.on_connect.add_observer(lambda: self.connected(activity))
        coros = [self.client.connect(), self.coroutine_queue.consume()]
        await asyncio.gather(*coros)
