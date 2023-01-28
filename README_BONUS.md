# Black Friday Orders Manager in Java BONUS

## Table of Contents

- [Black Friday Orders Manager in Java BONUS](#black-friday-orders-manager-in-java-bonus)
  - [Table of Contents](#table-of-contents)
  - [Synchronisation](#synchronisation)
    - [Orders](#orders)
    - [Products](#products)

## Synchronisation

### Orders

All of the **level 1** threads share the same file reader:

```java
public static BufferedReader ordersReader;
```

In order to not let two **master employees** read the same line, the logic of reading from the file is within a `synchronized` block:

```java
String orderRead;

try {
    synchronized (Tema2.ordersReader) {
        orderRead = Tema2.ordersReader.readLine();
    }
} catch (IOException e) {
    throw new RuntimeException(e);
}
```

This means that a **level 1** thread doesn't read all of the `orders.txt` file, it only reads what was dynamically given to it. I think
that this is more efficient than statically alocatting lines to each thread because some threads might be faster than others.
My method favours threads based on their computational speed.

### Products

All of the **level 2** threads spawned by a **level 1** thread share the same file reader, given by the **level 1** thread:

```java
BufferedReader productsReader;
```

By reading from the `order_products.txt` file the same way as the **level 1** threads, it is assured that no two **lesser employees**
read the exact same line for a given order.
This also means that each **level 2** thread spawned by the same **level 1** thread reads only a part of the file,
which makes the processing of the products file much faster than letting each **level 2** thread read from the start
of the file to the end of it, line by line:

```java
String currentProductRead;

try {
    synchronized (productsReader) {
     currentProductRead = productsReader.readLine();
    }
} catch (IOException e) {
    throw new RuntimeException(e);
}
```
