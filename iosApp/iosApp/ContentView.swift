import SwiftUI
import shared

struct ContentView: View {
	let greet = Greeting().greeting()
    let sum = KFetcher().addOne(i: BignumBigInteger.init(long: INT64_MAX))
    
    @State private var ip: String?
    var body: some View {
        Text(greet + "\nINT64_MAX + 1 = " + sum.toString(base: 10))
        Text(self.ip ?? "Loading...")
            .onAppear(perform: {
                KFetcher().fetch { kURLAndString in
                    print("Received (kURLAndString)")
                    print("Received (\(kURLAndString.url))")
                    print("Received (\(kURLAndString.string))")
                    self.ip = kURLAndString.string
                    return 456
                }
            })
        Text("1 - 1 = " + SFoo().decrement(i: BignumBigInteger.init(int: 1)).toString(base: 10))
    }
}

class SFoo: KFoo
{
    override func decrement(i: BignumBigInteger) -> BignumBigInteger {
        return i.dec()
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
