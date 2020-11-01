package net.zlw.cloud.excel.util;

public class RMBdeal {


    /**
     * 从小数点为分界线，往前迭代处理+往后拼接角分+部分规则
     * @param dbNum 随机浮点数值
     * @return  转化后的RMB大写数值描述
     */
    public String deal4RMB(String dbNum){
        String sb = "";
        try {
            int point = dbNum.lastIndexOf(".");
            char [] charArr = dbNum.toCharArray();
            for (int i = 1; i <= point; i++) {
                String flag = getUpperCase(charArr[point-i],i);
                sb = flag + sb ;
                if("error".equals(flag)){
                    return  "转化异常";
                }
            }
            sb = dealString(sb);
            sb = sb+"元"+getUpperCase(charArr[point+1],-1)+getUpperCase(charArr[point+2],-2);
        }catch (Exception e){
            System.out.println(e);
            return  "转化异常";
        }
        return  sb;
    }

    /**
     * 替换中文RMB计数 + 部分规则
     * @param a 数值
     * @param i 位置
     * @return  数值+位置
     */
    public String getUpperCase(char a,int i){
        //本位值为0  并且 本位位置在个位   小数点后一位  小数点后两位
        //不做任何返回操作
        if('0' == a && (i==1 || i==-1 || i==-2)){
            return "";
        }
        //本位值为0 并且 本位位置在万位
        //仅返回 万  而不是几万
        if ('0' == a && i==5) {
            return "万";
        }

        //本位数字转化的汉字大写数字
        String flag = "";
        //本位位置对应 【分 角 个 十 百 千  万  亿】
        String flag2 = "";

        switch (a){
            case '0' : flag = "零";break;
            case '1' : flag = "壹";break;
            case '2' : flag = "贰";break;
            case '3' : flag = "叁";break;
            case '4' : flag = "肆";break;
            case '5' : flag = "伍";break;
            case '6' : flag = "陆";break;
            case '7' : flag = "柒";break;
            case '8' : flag = "捌";break;
            case '9' : flag = "玖";break;

            default : flag = "error";
        }

        //本位为零  不拼接本位位置 【除万位  上方已经单独处理】
        if(!"零".equals(flag)){
            switch (i){
                case -1: flag2 = "角";break;
                case -2: flag2 = "分";break;
                case 2:
                case 6:
                case 10: flag2 = "十";break;

                case 3:
                case 7:
                case 11: flag2 = "百";break;

                case 4:
                case 8: flag2 =  "千";break;

                case 5: flag2 = "万";break;
                case 9: flag2 = "亿";break;

                default: flag2 = "";
            }
        }


        /**
         * 本处处理未完善
         *因为随机数 Integer位数达到10位以上的概率很大
         *因此处理时仅处理了 壹十贰亿几千几百万 这种情况
         *若随机数级别在十万位 依旧会出现  壹十几万几千几百
         *若随机数级别在十位   依旧会出现  壹十几点几情况
         *
         * 若要完全处理，仅需要先判断随机数总位数，而后再判断i的值为第几位再特殊处理即可~~
         */
        if("壹".equals(flag) && "十".equals(flag2) && i == 10){
            return  flag2;
        }

        //返回 数值+位置
        return flag+flag2;
    }


    /**
     * 去除重复零  + 部分规则
     * @param rmbStr
     * @return
     */
    public String dealString(String rmbStr){
        while(rmbStr.indexOf("零零") >= 0){
            rmbStr = rmbStr.replace("零零","零");
        }

        //判断个位是否依旧为零 若为零  截取字符串即可
        char [] arr = rmbStr.toCharArray();
        if('零' == arr[arr.length-1]){
            rmbStr = rmbStr.substring(0,rmbStr.length()-1);
        }
        return rmbStr;
    }
}
