# Learn Java by its pitfalls

This document covers the most common sources of mistakes using Java.
It is not intended to be a reference for the Java programming language.

## Hands-on samples with unit tests

A collection of tiny samples is available in `be.ecam.basics.samples.Samples` with unit tests in `basics/src/test/java/be/ecam/basics/samples/SamplesTest.java`.
They illustrate common pitfalls such as:
- casting and integer overflow/underflow
- integer division truncation
- floating-point precision (IEEE 754)
- NaN comparison behavior
- null handling and NullPointerException (checked vs unchecked)
- checked exceptions enforcement
- reference aliasing and mutability
- string reference equality (==) vs content equality (equals)
- autounboxing null leading to NPE

How to run the tests for this module only:
- Using Gradle Wrapper from the project root: `gradlew :basics:test`
- Or from the `basics` directory: `gradlew test`

## The Java types

Although Java possesses a static type system, there are some dangers that developer should be aware off.
Some typical mistakes can be avoided by simply being aware of these dangers.

Java distinguishes between two kind of types: primitive types and references types (aka "objects").

### Primitive types

| type        |      description      | size in bits  | default value |
|-------------|:---------------------:|---------------|---------------|
| **byte**    |        integer        | 8             | 0             |
| **short**   |        integer        | 16            | 0             |
| **int**     |        integer        | 32            | 0             |
| **long**    |        integer        | 64            | 0             |
| **float**   | floating point number | 32            | 0             |
| **double**  | floating point number | 64            | 0             |
| **boolean** |        Boolean        | JVM dependant | `false`       |
| **char**    |   Unicode character   | 16            | '\u0000'      |

A primitive type value is always passed by copy as parameter for a method or constructor.
They have a known fixed size. They default to some value if not explicitly initialized.

There are a few common mistakes when dealing with primitive types, notably:

- All integer and floating point types are **signed**. Integers are encoded as **two's complement**.
  See <https://en.wikipedia.org/wiki/Two%27s_complement>.
- Integers overflow and underflow by "wrapping around".
  It might, for instance, be surprising to note that the expression `(byte) 128` evaluates to `-128`.
  This is because `Byte.MAX_VALUE` is `127`, which then "wraps around" to `Byte.MIN_VALUE` of `-128`.
- Floating points obey the `IEEE 754` standard.
  They have, as their name suggest, a floating precision.
  As such, they are not meant for usage where you need an exact value (for instance representing division of a currency)
  .

### Reference types

Non-primitive types are called reference types. This includes arrays, `String`s and even classes themselves.
A reference can be seen as a JVM pointer to the heap of your Java program.
The heap stores all object instance fields of the instance, which might be primitive types or references types
themselves, referencing in turn other portion of the heap.
The heap is essentially a shared memory location amongst all the program threads.

References types are allocated using the `new` keyword.
Reference types are subject to garbage collection.
As a first approximation, we can say that a reference is candidate for garbage collection if and only if it is not
referenced by the program.

The default value of a reference is `null`.

## Namespaces

Like most modern languages, Java possesses the ability to define Namespace to avoid name clashing.

The canonical way of organising Java namespaces is through the usage of packages.
A java package is a group of named types (classes, interfaces and enums).
Incidentally, a package is also the directory path of the file system leading to the files containing the said type
definitions.

By convention, packages are organized to form an inverse domain name, like so:

- org.example
- java.lang
- be.ecam

This convention is enforced if you need to publish your code as a library.
In this case, you might need to own the domain name of your published package.

Additionally, a class, an interface or an enum is also a namespace.
If you take the `Object` class from the Java standard library, it's FQDN is `java.lang.Object`.

Although technically possible, it is considered bad practice to have java source code not living within a package.

## The `Object` class

All Java classes has the ability to inherit from on parent class. All java objects share one common same ancestor,
the `Object` class.

The `Object` class defines some useful methods that can be overridden.

### `toString()` method

This method is used to provide a human-readable representation of your object.
The default implementation will simply print the name of the instance class followed by its hashcode.
A lot of other classes might invoke `toString()`, like `System.out.print()` or `Formatter`.
It might be interesting to override this method for ease of debugging, logging or provide better error messages.

### `equals()` and `hashcode()`

`equals()` is used to compare two reference types between each other.
Default implementation of `equals()` is to check whether the two references are the same.
**It does not automatically compare the object internal fields!**

Common error is to override `equals()` but not `hashCode()`.
The issue with that is that your class will be incorrectly handled with other classes using both methods.
This includes `HashMap` and `HashSet`.
If `obj1.equals(obj2)` is `true`, then `obj1.hashcode() == obj2.hashcode` ***must*** be `true`.

### `finalize()`

`finalize()` is not the same thing as a destructor.
Because garbage collection is non-deterministic by definition, there is no guarantee about the order of execution of different `finalize()` from different objects.

As a side note, RAII is not applicable to Java when dealing with resources.
The proper ways to release resources is by using either:
- `try-finally` blocks
- `try-with` (Assuming the resource implements `Closeable` or `AutoCloseable`)

## Exceptions and Errors

Java uses Exceptions and Errors, both inheriting from a common class `Throwable`.
All `Throwable` objects can be "thrown" using the `throw` keyword.
The exception (or error), will then cause a "stack unwinding", bubbling up from method calls to method calls until one
of this happens:

- The `Throwable` is intercepted by a `try {} catch {}` block
- There is no more stack frame to unwind. The JVM stops.

### Errors

Errors are representing conditions that are usually not recoverable.

For instance a `JavaOutOfMemoryError` indicates that not enough memory space could be found on the heap for an
allocation.
Usually, there is no recovery from that at runtime. This could either be a bug (memory leak) or even simply that the max
JVM heap size needs to be tuned.
In such case, catching the error could still be useful for logging purposes or closing some opened resources.

### Checked exceptions vs Runtime exceptions

Runtime exception extends from `RuntimeException` class. Checked exceptions do not.

The Java compiler enforces that checked exceptions need to be handled by the calling code, either by:
- catching the exception.
- indicating that the same exception can be thrown in the calling method signature.

### Handling `null`

A `NullPointerException` occurs when trying to invoke a method on a reference type that is null.
It is also possible to throw this exception willingly.

Java's `NullPointerException` is a `RuntimeException`, meaning that it is not checked. cfr above.
This means that the compiler does not protect against null usage.

Correct `null` handling is a common source of mistake.
By default, always assume that a reference type might be null.

Common ways to handle null are:
- basic null check
- usage of type annotation `@Nullable`, `@NotNull`
- usage of `Optional<T>` as return type from a method

## Exercises

A set of 10 small, realistic exercises lives under the package `be.ecam.basics.exercises`.
Their unit tests are in `basics/src/test/java/be/ecam/basics/exercises`.
Some of these tests intentionally fail at first to reflect typical real‑world bugs; your goal is to make them pass by improving the production code.

How to run only this module’s tests:
- From project root: `gradlew :basics:test`
- From the `basics` directory: `gradlew test`
