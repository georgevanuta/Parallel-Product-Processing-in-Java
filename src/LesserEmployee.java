import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

public class LesserEmployee extends RecursiveAction {
	final String ORDER_ID;
	BufferedReader productsReader;

	public LesserEmployee(
			final String ORDER_ID,
			BufferedReader productsReader
	) {

		this.ORDER_ID = ORDER_ID;
		this.productsReader = productsReader;
	}


	@Override
	protected void compute() {

		String currentProductRead;

		while (true) {
			try {
				synchronized (productsReader) {
					currentProductRead = productsReader.readLine();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			final String CURRENT_PROD_ID = currentProductRead.split(",")[0];

			if (CURRENT_PROD_ID.equals(ORDER_ID)) {
				try {
					synchronized (Tema2.productsWriter) {
						Tema2.productsWriter.write(currentProductRead + ",shipped\n");
					}
				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				return;
			}

		}
	}
}
