# Genome Financial Pixel

[![Release](https://jitpack.io/v/genome-eu/jfpx.svg)](https://jitpack.io/#genome-eu/jfpx)

General FPX/HPP signature generation reference implementation.

Compatible with any Java 8+ Server/Desktop/Android application. Zero dependencies.

# Distribution

Builds of this repository can be obtained from [JitPack](https://jitpack.io/#genome-eu/jfpx).

Add JitPack repository:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then add dependency:

```xml
<dependency>
    <groupId>com.github.genome-eu</groupId>
    <artifactId>jfpx</artifactId>
    <version>1.0.0</version>
</dependency>
```

# Prerequisites

1. Register on https://my.genome.eu
2. Pass KYC procedure
3. Create business wallet (if required)
4. Create HPP/FPX and obtain API key/secret pair

# Usage

This library contains two helper classes to work with signature, any of it can be used:

### SignatureGenerator

Immutable helper, configured by secret in constructor.

```java
SignatureGenerator generator = new SignatureGenerator("<api secret>");
Instant current = Instant.now();
String signature = generator.MODE_A_TS(
    current,
    9.99,
    "EUR",
    "12345", // Optional
    "67890", // Optional
    "5137"   // Optional
);
```


### SignatureBuilder

Mutable builder.

```java
SignatureGenerator generator = new SignatureGenerator("<api secret>");
Instant current = Instant.now();
String signature = new SignatureBuilder()
    .mode(SignatureMode.MODE_A_TS)
    .nonce(current)
    .amount(9.99, "EUR")
    .orderId("12345")
    .userId("67890")
    .mcc(5137)
    .build(generator); // or .build("<api secret")
```