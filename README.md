# Black Friday Orders Manager in Java

*Score*: 100 / 100

## Table of Contents

- [Black Friday Orders Manager in Java](#black-friday-orders-manager-in-java)
  - [Table of Contents](#table-of-contents)
  - [Main Thread](#main-thread)
  - [Level 1 Threads](#level-1-threads)
  - [Level 2 Threads](#level-2-threads)
  - [Synchronisation](#synchronisation)

## Main Thread

The Main class `Tema2.java` has the responsability of creating all of the **level 1** threads `MasterEmployee` and putting them in a
pool of threads with a fixed size of **P** (`masterPool`).
Then, it waits for all **level 1** threads finish in order to not close the output files before any of them finish processing them.
The method which puts **level 1** threads into the pool is called:

```java
private static void InitThreadHandlers(final String[] args)
```

and the logic behind it is explained in the following code snippet:

```java
// initialize the level 1 threads
ArrayList<MasterEmployee> masterEmployees =
  new ArrayList<>(
    Stream.generate(() -> new MasterEmployee())
      .limit(P)
      .collect(Collectors.toList())
  );

// submit them
for (MasterEmployee masterEmployee : masterEmployees) {
   masterPool.submit(masterEmployee);
}

// wait for them to finish before closing the output files
for (MasterEmployee masterEmployee : masterEmployees) {
    masterEmployee.join();
}
```

## Level 1 Threads

A **level 1** thread is represented by the `MasterEmployee.java` class. This type of thread reads lines from the `orders.txt` file and
then starts spawning into the **level 2** thread pool (`lesserPool`), which also has a fixed size of **P** and is shared between **all level 1**
threads. It then waits for its **level 2** threads to finish before "shipping" its order.

```java
// initialize the level 2 threads
ArrayList<LesserEmployee> lesserEmployees =
    new ArrayList<>(
     Stream.generate(() -> new LesserEmploye(ORDER_ID, productsLeft, productsReader))
        .limit(NR_PRODUCTS)
        .collect(Collectors.toList())
     );

// submit them
for (LesserEmployee lesserEmployee : lesserEmployees) {
    Tema2.lesserPool.submit(lesserEmployee);
}

// wait for them to finish before shipping the order
for (LesserEmployee lesserEmployee : lesserEmployees) {
    lesserEmployee.join();
}
```

## Level 2 Threads

A **level 2** thread is represented by the `LesserEmployee.java` class. This thread works by reading the `order_products_out.txt` file
line by line. Once it finds a product corresponding to the **level 1** thread that spawned it, it "ships" the product before finishing
its execution.

## Synchronisation

This part is explained in the [`README_BONUS.md`](/README_BONUS.md) file.
