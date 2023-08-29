# instagram2pixelfed - Instagram cross-posting tool

Command-line runner for cross-posting instagram posts to any Mastodon and Pixelfed.

## How to use

A docker image is provided and can be setup to run periodically depending on your needs.

```bash
docker run ghcr.io/dsi-vd/instagram2fedi:<tag> --env-file .env
```

At least three environment variables need to be defined for the application to run,
`INSTAGRAM_TARGET_USERNAME`, `MASTODON_INSTANCE` and `MASTODON_ACCESS_TOKEN`.

`INSTAGRAM_TARGET_USERNAME` is the username of the Instagram account you want to cross-post from.

`MASTODON_INSTANCE` is the instance you want to cross-post to.

`MASTODON_ACCESS_TOKEN` is the access token you can get from your Mastodon instance.

### Other variables

`INSTAGRAM_POST_COUNT` is the number of posts to fetch from Instagram. Defaults to 5.

`MASTODON_MAX_CAROUSEL_SIZE` is the maximum number of images in a single post.
Use this if your instance restricts this. Defaults to 4.

`MASTODON_CONTENT_LANGUAGE` is the ISO-639 code for the language of the content
you are posting. Defaults to `fr`.

`MASTODON_CACHE_FILE` is the path to the cache file where this script saves a reference 
to the posts that have already been send. Defaults to `/tmp/mastodon.cache`.

## Configuration

```dotenv
# Instagram
INSTAGRAM_TARGET_USERNAME=account_name
INSTAGRAM_POST_COUNT=5

# Mastodon
MASTODON_INSTANCE=https://pixelfed.social/
MASTODON_ACCESS_TOKEN=jwt_token
MASTODON_MAX_CAROUSEL_SIZE=4
MASTODON_CONTENT_LANGUAGE=fr
MASTODON_CACHE_FILE=persistent/mastodon.cache
```
