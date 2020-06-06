/*

\\.                   相当于.
\\w                 匹配任何字类字符，包括下划线。相当于"[A-Za-z0-9_]"
[A-Za-z0-9]  匹配任何数字和字母，不包括下划线
\\d                 相当于[0-9]
?                   0次或1次匹配前面紧贴着的字符或表达式，相当于{0,1}
+                   1次或多次匹配前面紧贴着的字符或表达式，相当于{1,}
*                    0次或多次匹配前面紧贴着的字符或表达式，相当于{0,}
^                    字符串开始的位置
$                    字符串结束的位置
{n}                  匹配n次
{n,}                 匹配n次或多次   
{n,m}              匹配至少n次，至多m次


 */

package net.xfunction.java.api.core.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串工具类
 * 
 * @author Kelvin
 */
public class BaseUtils 
{
    /** 空字符串 */
    private static final String NULLSTR = "";



    /**
     * 获取参数不为空值
     * 
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    public static <T> T nvl(T value, T defaultValue)
    {
        return value != null ? value : defaultValue;
    }

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     * 
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll)
    {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     * 
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll)
    {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     * 
     * @param objects 要判断的对象数组
     ** @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects)
    {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     * 
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects)
    {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     * 
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }

    /**
     * * 判断一个字符串是否为空串
     * 
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str)
    {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     * 
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    /**
     * * 判断一个对象是否为空
     * 
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     * 
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

    /**
     * * 判断一个对象是否是数组类型（Java基本型别的数组）
     * 
     * @param object 对象
     * @return true：是数组 false：不是数组
     */
    public static boolean isArray(Object object)
    {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * 去空格
     */
    public static String trim(String str)
    {
        return (str == null ? "" : str.trim());
    }

    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = str.length() + start;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (start > str.length())
        {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     * 截取字符串
     * 
     * @param str 字符串
     * @param start 开始
     * @param end 结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (end < 0)
        {
            end = str.length() + end;
        }
        if (start < 0)
        {
            start = str.length() + start;
        }

        if (end > str.length())
        {
            end = str.length();
        }

        if (start > end)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (end < 0)
        {
            end = 0;
        }

        return str.substring(start, end);
    }

  

    /**
     * 是否包含字符串
     * 
     * @param str 验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs)
    {
        if (str != null && strs != null)
        {
            for (String s : strs)
            {
                if (str.equalsIgnoreCase(trim(s)))
                {
                    return true;
                }
            }
        }
        return false;
    }
    
   
    
    public static String change86phone(String phone) throws Exception {
		if (!isEmpty(phone)) {
			
				if (!phone.contains("+")) {
					phone = "+86" + phone;
				}
			
		}
		return phone;
	}
   
    public static String replaceMobile(String phone) throws Exception {
		if (!isEmpty(phone)) {
			if (phone.contains("+")) {
				phone = phone.substring(3);
			}
			if (phone.length() != 11) {
				phone = "";
			}
		}
		
		return phone;
	}
    
    public static String getInviteCode(Integer length) {
        String str="0123456789";

        Random random=new Random();

        StringBuffer sb=new StringBuffer();

        for(int i=0;i<length;i++){

            int number=random.nextInt(10);

            sb.append(str.charAt(number));

        }

        return sb.toString();
	}    
    
    
    
    public static boolean isPhone(String phone) {
    	if(isEmpty(phone)) return false;   
    	
        String regex = "^1\\d{10}$";
        if (phone.length() != 11) {            
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            return  m.matches();                        
        }
    }
    
    public static boolean isMail(String mail) {
    	
    	if(isEmpty(mail)) {
    		return false;
    	}else {
    		String regex = "^[a-zA-Z][a-zA-Z0-9_.+-=:]+@\\b(?:[0-9A-Za-z][0-9A-Za-z-]{0,62})(?:\\.(?:[0-9A-Za-z][0-9A-Za-z-]{0,62}))*(\\.?|\b)$";
    		//Pattern pattern=Pattern.compile(regex);
    		//Matcher matcher=pattern.matcher(mail);
    		//return matcher.matches();
    		//return matcher.find();
    		return mail.matches(regex);    		
    	}

    }
    /*
     * 6至20位，以字母开头，字母，数字，减号，下划线
     */
    public static boolean isName(String name) {
    	if(isEmpty(name))  return false;
    	String regex = "^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+$";
    	return name.matches(regex);
    }
    
    /**
           最短6位， {6,}
	必须包含1个数字
	必须包含1个小写字母
	必须包含1个大写字母
	必须包含1个特殊字符
     *
     * @param password 密码
     * @return
     */
    public static boolean isPassword(String password) {
    	if(isEmpty(password))  return false;    	
        String regex = "^.*(?=.{6,})(?=.*\\d)(?=.*[A-Z]{1,})(?=.*[a-z]{1,})(?=.*[~!@#$%^&*()\\\\_+`\\-={}:\";'<>?,\\[\\] .\\/]).*$";
        System.out.println(regex);
        return password.matches(regex);    	
    }

    /**
     * 是否含有特殊字符
     * @param str
     * @return
     */
    public static boolean hasSpecialChar(String str) {
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern=Pattern.compile(regEx);
        Matcher matcher=pattern.matcher(str);
        return  matcher.find();
    }

    /**
     * 字符串中是否包含emoji
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        boolean isEmoji = false;
        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i + 1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d
                        || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c
                        || hs == 0x2b1b || hs == 0x2b50 || hs == 0x231a) {
                    return true;
                }
                if (!isEmoji && source.length() > 1 && i < source.length() - 1) {
                    char ls = source.charAt(i + 1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return isEmoji;
    }


    

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }


    /**
     * 去除html标签
     * @param content 字符串
     * @return
     */
    public static String delHtmlTag(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");

        content = content.replaceAll("\r|\n|\t", "");
        // 去掉空格
        content = content.replaceAll(" ", "");

        return content;
    }
    
    /*
    public static String maskUserName(String userName) {
    	String result = userName.replaceAll(".", "*");
    	result = userName.charAt(0) + result.substring(1, userName.length()-1) + userName.charAt(userName.length()-1);
    	return result;
    }
    */
    
    public static void main(String[] args) {

    	
    	// System.out.println(maskUserName("kelvin"));

	}
}