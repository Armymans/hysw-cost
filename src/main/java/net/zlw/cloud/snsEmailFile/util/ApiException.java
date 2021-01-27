package net.zlw.cloud.snsEmailFile.util;

/**
 * API客户端异常。
 * @author zhaidawei@ruibetter.com
 */
public class ApiException extends Exception {

	private static final long serialVersionUID = -238091758285157331L;

	private String code;
	private String msg;

    public ApiException() {
		super();
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

	public ApiException(String code, String msg) {
		super(code + ":" + msg);
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
