import SwiftUI
import shared

struct ContentView: View {
	let greet = Greeting().greeting()
    let sum = KFetcher().addOne(i: BignumBigInteger.init(long: INT64_MAX))

	var body: some View {
        Text(greet + "\nINT64_MAX + 1 = " + sum.toString(base: 10))
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
