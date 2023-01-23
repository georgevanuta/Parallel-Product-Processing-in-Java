import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MasterEmployee extends RecursiveAction {
	@Override
	protected void compute() {
		while (true) {
			String orderRead;

			try {
				synchronized (Tema2.ordersReader) {
					orderRead = Tema2.ordersReader.readLine();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			if (orderRead == null) return;


			final String ORDER_SPLIT[] = orderRead.split(",");

			final String ORDER_ID = ORDER_SPLIT[0];
			final Integer NR_PRODUCTS = Integer.parseInt(ORDER_SPLIT[1]);

			if (NR_PRODUCTS <= 0) return;

			BufferedReader productsReader;

			try {
				productsReader = new BufferedReader(new FileReader(Tema2.PRODUCTS_FILE));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}

			ArrayList<LesserEmployee> lesserEmployees =
					new ArrayList<>(
							Stream.generate(() -> new LesserEmployee(ORDER_ID, productsReader))
									.limit(NR_PRODUCTS)
									.collect(Collectors.toList())
					);

			for (LesserEmployee lesserEmployee : lesserEmployees) {
				Tema2.lesserPool.submit(lesserEmployee);
			}

			for (LesserEmployee lesserEmployee : lesserEmployees) {
				lesserEmployee.join();
			}

			try {
				synchronized (Tema2.ordersWriter) {
					Tema2.ordersWriter.write(orderRead + ",shipped\n");
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
