package net.zlw.cloud.common;


import java.util.HashMap;
import java.util.Map;

public class RestUtil {
	public static Map<String, Object> success() {
		Map map = new HashMap();
		map.put("code", "M000000");
		map.put("info", "操作成功");
		return map;
	}

	public static Map<String, Object> success(Object data) {
		Map map = new HashMap();
		map.put("code", "M000000");
		map.put("info", "操作成功");
		map.put("data", data);
		return map;
	}

	public static Map<String, Object> error() {
		Map map = new HashMap();
		map.put("code", "E000000");
		map.put("info", "操作失败");
		return map;
	}

	public static Map<String, Object> success(String msg) {
		Map map = new HashMap();
		map.put("code", "M000000");
		map.put("info", msg);
		return map;
	}

	public static Map<String, Object> error(String msg) {
		Map map = new HashMap();
		map.put("code", "E000000");
		map.put("info", msg);
		return map;
	}

	public static Map<String, Object> showError(Object msg) {
		Map map = new HashMap();
		map.put("flag", "F");
		map.put("msg", msg);
		return map;
	}
	
	public static Map<String, Object> showSucc(Object msg) {
		Map map = new HashMap();
		map.put("flag", "T");
		map.put("msg", msg);
		return map;
	}
	
	public static Map<String, Object> showParam(String flag, Object msg, Object param1){
		Map map = new HashMap();
		map.put("flag", flag);
		map.put("msg", msg);
		map.put("param1", param1);
		return map;
	}
	
	public static Map<String, Object> showJsonSuccess(Object data){
		Map map = new HashMap();
		map.put("code", "M000000");
		map.put("info", "success");
		map.put("data", data);
		return map;
	}
	
	public static Map<String, Object> showJsonError(String info){
		Map map = new HashMap();
		map.put("code", "E000000");
		map.put("info", info);
		return map;
	}
}