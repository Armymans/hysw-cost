package net.zlw.cloud.common;


import com.github.pagehelper.PageInfo;
import net.zlw.cloud.clearProject.model.ClearProject;

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
	/**
	 * code: "M000000"
	 * count: 15
	 * data: [{id: "05cf9127c68842e38b33d9426428c818", designUnitName: "fgvgfg", status: "1", contact: "bdgfb",…},…]
	 * info: "操作成功"
	 * pageNum: "1"
	 * pageSize: "15"
	 * totalCount: 3
	 * totalPageNum: 1
	 */
	public static Map<String,Object> page(PageInfo<?> obj){
		Map map = new HashMap();
		map.put("code", "M000000");
		map.put("count", obj.getPageSize());
		map.put("data", obj.getList());
		map.put("info", "操作成功");
		map.put("pageNum", obj.getPageNum());
		map.put("pageSize", obj.getPageSize());
		map.put("totalCount", obj.getTotal());
		map.put("totalPageNum", obj.getPages());
		return map;
	}
}