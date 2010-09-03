package example.callbacks;


class Caller {

	public void register(CallBack callback) {
		callback.methodToCallBack();
	}

	public static void main(String[] args) {
		CallBack callBack = new CallBackImpl();

		Caller caller = new Caller();
		caller.register(callBack);
	}

}
