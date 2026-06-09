# SolLogger

----

Improved logger.

---

Make a new instance by giving it a name.

Has the methods `info`, `warn`, `debug` and `error`. They all take as many arguments as you want, log them all on a single line separated by spaces, and then returns back the first argument.

Also displays its name in the console for easier tracking.

`debug` will only ever print something in test environments, not when exported, and will never print the same thing twice in a row.

```java
// Should be stored as a constant
SolLogger logger = new SolLogger("Sol/Test");

int x = logger.info(3 + 5, ": x value");  // x is 8
```

> [Sol/Test] 8 : x value

Every SolModContainer automatically creates a SolLogger with its name accessible by `getLogger()`.
