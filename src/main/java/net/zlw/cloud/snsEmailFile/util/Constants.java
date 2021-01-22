package net.zlw.cloud.snsEmailFile.util;

/**
 * API公用常量类。
 * @author zhaidawei@ruibetter.com
 */
public abstract class Constants {

	/** 协议入参共享参数 **/
	public static final String APP_KEY = "app_key";
	public static final String TIMESTAMP = "timestamp";
	public static final String SIGN = "sign";
	public static final String SIGN_METHOD = "sign_method";
	public static final String SIMPLIFY = "simplify";
	public static final String REQUEST_MD5 = "request_md5";
	
	/** 协议出参共享参数 */
	public static final String ERROR_RESPONSE = "error_response";
	public static final String ERROR_CODE = "code";
	public static final String ERROR_MSG = "msg";

	/** 默认时间格式 **/
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/** Date默认时区 **/
	public static final String DATE_TIMEZONE = "GMT+8";

	/** UTF-8字符集 **/
	public static final String CHARSET_UTF8 = "UTF-8";

	/** HTTP请求相关 **/
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	public static final String CTYPE_FORM_DATA = "application/x-www-form-urlencoded";
	public static final String CTYPE_FILE_UPLOAD = "multipart/form-data";
	public static final String CTYPE_TEXT_XML = "text/xml";
	public static final String CTYPE_TEXT_PLAIN = "text/plain";
	public static final String CTYPE_APP_JSON = "application/json";
	
	/**HTTP请求超时时间**/
	public static final int CONNECTION_TIMEOUT=15000; // 默认连接超时时间为15秒
	public static final int READ_TIMEOUT=30000;//默认读取超时时间为30秒

	/** JSON格式 */
	public static final String FORMAT_JSON = "json";
	/** XML格式 */
	public static final String FORMAT_XML = "xml";

	/** MD5签名方式 */
	public static final String SIGN_METHOD_MD5 = "md5";
	/** HMAC签名方式 */
	public static final String SIGN_METHOD_HMAC = "hmac";

	/** 响应编码 */
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String CONTENT_ENCODING_GZIP = "gzip";

	/** 默认媒体类型 **/
	public static final String MIME_TYPE_DEFAULT = "application/octet-stream";

	/** 默认流式读取缓冲区大小 **/
	public static final int READ_BUFFER_SIZE = 1024 * 4;
	
}
