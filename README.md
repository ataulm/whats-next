# what's next

android letterboxd client offering a subset of [letterboxd](https://letterboxd.com) functionality with a focus on inclusive design.

## Running

You'll need to add these properties somewhere that Gradle can read them (e.g. in `~/.gradle/gradle.properties`):

```
# Letterboxd
letterboxd_api_key=12345678
letterboxd_api_secret=12345678
```

## API
The API is in closed beta and [documentation](https://api-docs.letterboxd.com/) may not be up to date. It might also be necessary to have your API key allow-listed to permit the `content:modify` scope (for write access).

- Auth is new (body: user, pass, grant type, client id, client secret and scope; nonce and timestamp no longer needed as query params)
- All auth'd read/write APIs are new (Authorization Bearer with new scoped auth token, no need for nonce/timestamp)
- Non-authenticated read APIs can be authorized with client auth token - there's a new API to fetch this
