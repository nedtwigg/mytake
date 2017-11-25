# codename mytake.org

[![Travis CI](https://travis-ci.org/mytake/mytake.svg?branch=master)](https://travis-ci.org/mytake/mytake)
[![License GPLv2](https://img.shields.io/badge/license-GPLv2-brightgreen.svg)](https://tldrlegal.com/license/gnu-general-public-license-v2)
[![Live chat](https://img.shields.io/badge/gitter-chat-brightgreen.svg)](https://gitter.im/mytake/mytake)

Here's what we have so far:

- [A pitch](https://github.com/mytake/mytake/wiki/Pitch)
- [A design for our product](https://github.com/mytake/mytake/wiki/Design)
- [A prototype for our product](https://mytake.netlify.com/)
- We're discussing how to improve these [here](https://github.com/mytake/mytake/issues).

# build instructions

## Quickstart

Run `gradlew live`, and you'll get:

- a server running at localhost:8080
- proxied by browsersync, with instant asset reloading
- and a continuous build hotswapping any code or template changes
- **exit by navigating to `/exit` or you'll have zombie processes all over the place**
    + `./kill_zombies.sh` will save you if you have zombies

Takes 30-60s to launch.

## styling

styling is all in `/client/assets/stylesheets/main.scss`

## client-side js

all js lives in `/client/src`

## server-side java

all lives in `/server/`

## CI

- Travis runs `gradlew check`

## update deps

Update gradle deps with `gradlew dependencyUpdates` ([ref](https://github.com/ben-manes/gradle-versions-plugin)).

Update npm deps in client folder with

```
npm outdated               - shows which packages are out of date
npm update <packagename>   - updates packagename to "Wanted", but won't pass semver
```

## troubleshooting

OUTDATED: gradle handled NPM now, TBD how we recommend using it

If `npm install` generates EINTEGRITY warnings, try the following

```
npm install -g npm@5.2.0
rm package-lock.json
rm -rf node_modules/
npm cache clear --force
npm install
```

npm5 has some key features, but apparently lots of bugs.
