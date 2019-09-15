# HangupsDroid

[![No Maintenance Intended](http://unmaintained.tech/badge.svg)](http://unmaintained.tech/)

HangupsDroid is a port of [hangups](https://hangups.readthedocs.io/en/latest/) to Android.

It allows you to use Hangouts on an Android phone without Play Services.

![Screenshot](screenshot.png)

## 2-Step Verification

hangups only supports the following 2FA methods:

* OTP (Google Authenticator)
* SMS

If none of these methods are available for your account, login will fail with `GoogleAuthError: Authorization code cookie not found`.

## Warning

Never give your Google account credentials to any application or device that you don’t trust. Logging into Google grants hangups unrestricted access to your account. hangups works this way because Google does not provide any other method to access the Hangouts API.
