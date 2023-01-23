import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tema2 {

	private static String ORDERS_FILE = "orders.txt";
	public static String PRODUCTS_FILE = "order_products.txt";

	public static BufferedReader ordersReader;

	public static ForkJoinPool lesserPool;

	public static BufferedWriter ordersWriter;
	public static BufferedWriter productsWriter;

	private static void InitInputFiles(final String[] args) {
		Misc.exitIf(
				args.length != 2,
				"[USAGE]: java Tema2 <INPUT_DIR> <NR_MAX_THREADS>"
		);

		final String INPUT_DIR = args[0];
		ORDERS_FILE = INPUT_DIR + "/" + ORDERS_FILE;
		PRODUCTS_FILE = INPUT_DIR + "/" + PRODUCTS_FILE;
	}

	private static void InitOutputHandlers() {
		final File ORDERS_OUT = new File("orders_out.txt");
		final File PRODUCTS_OUT = new File("order_products_out.txt");

		try {
			ORDERS_OUT.createNewFile();
			PRODUCTS_OUT.createNewFile();

			ordersReader = new BufferedReader(new FileReader(ORDERS_FILE));
			ordersWriter = new BufferedWriter(new FileWriter(ORDERS_OUT, false));
			productsWriter = new BufferedWriter(new FileWriter(PRODUCTS_OUT, false));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void InitThreadHandlers(final String[] args) {
		final Integer P = Integer.parseInt(args[1]);

		ForkJoinPool masterPool = new ForkJoinPool(P);
		lesserPool = new ForkJoinPool(P);

		ArrayList<MasterEmployee> masterEmployees =
				new ArrayList<>(
						Stream.generate(() -> new MasterEmployee())
								.limit(P)
							  	.collect(Collectors.toList())
				);

		for (MasterEmployee masterEmployee : masterEmployees) {
			masterPool.submit(masterEmployee);
		}

		for (MasterEmployee masterEmployee : masterEmployees) {
			masterEmployee.join();
		}
	}

	private static void CloseHandlers() {
		try {
			ordersWriter.close();
			productsWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) {

		InitInputFiles(args);

		InitOutputHandlers();

		InitThreadHandlers(args);

		CloseHandlers();
	}
}
