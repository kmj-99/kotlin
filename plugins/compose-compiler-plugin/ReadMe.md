Experimental build of compose compiler plugin against the latest master.

Run tests:
```bash
gradle -p . -Pkotlin.build.compose.publish.enabled=true cleanTest test
```

Experimental publishing:

```bash
gradle -p . -Pkotlin.build.compose.publish.enabled=true publishToMavenLocal
```
