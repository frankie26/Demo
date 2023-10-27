import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedList;

public class Calculator {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        BigDecimal result = calculator.calculate("1", "1", '+');
        System.out.println("第一次计算，当前计算式是："+calculator.getExpression());
        System.out.println("第一次计算，当前结果是："+result);
        System.out.println("第一次计算，当前结果是："+calculator.getResult());
        System.out.println("");

        BigDecimal result1 = calculator.calculate("2", "2", '-');
        System.out.println("第二次计算，当前计算式是："+calculator.getExpression());
        System.out.println("第二次计算，当前结果是："+result1);
        System.out.println("第二次计算，当前结果是："+calculator.getResult());
        System.out.println("");

        BigDecimal result2 = calculator.calculate("3", "3", '*');
        System.out.println("第三次计算，当前计算式是："+calculator.getExpression());
        System.out.println("第三次计算，当前结果是："+result2);
        System.out.println("第三次计算，当前结果是："+calculator.getResult());
        System.out.println("");

        BigDecimal result3 = calculator.calculate("4", "4", '/');
        System.out.println("第四次计算，当前计算式是："+calculator.getExpression());
        System.out.println("第四次计算，当前结果是："+result3);
        System.out.println("第四次计算，当前结果是："+calculator.getResult());
        System.out.println("");

        BigDecimal result4 = calculator.calculate("5", "5", '*');
        System.out.println("第五次计算,当前结果是："+calculator.getResult());
        System.out.println("第五次计算,当前计算式是："+calculator.getExpression());
        System.out.println("第五次计算,当前结果是："+result4);
        System.out.println("");

        System.out.println("=============");

        calculator.undo();
        System.out.println("undo第一次，当前计算式是："+calculator.getExpression());
        System.out.println("undo第一次，当前结果是："+calculator.getResult());
        System.out.println("");

        calculator.undo();
        System.out.println("undo第二次，当前计算式是："+calculator.getExpression());
        System.out.println("undo第二次，当前结果是："+calculator.getResult());
        System.out.println("");

        calculator.undo();
        System.out.println("undo第三次，当前计算式是："+calculator.getExpression());
        System.out.println("undo第三次，当前结果是："+calculator.getResult());
        System.out.println("");

        calculator.undo();
        System.out.println("undo第四次，当前计算式是："+calculator.getExpression());
        System.out.println("undo第四次，当前结果是："+calculator.getResult());
        System.out.println("");

        calculator.undo();
        System.out.println("undo第五次，当前计算式是："+calculator.getExpression());
        System.out.println("undo第五次，当前结果是："+calculator.getResult());
        System.out.println("");


        calculator.redo();
        System.out.println("redo第一次，当前计算式是："+calculator.getExpression());
        System.out.println("redo第一次，当前结果是："+calculator.getResult());
        System.out.println("");

        calculator.redo();
        System.out.println("redo第二次，当前计算式是："+calculator.getExpression());
        System.out.println("redo第二次，当前结果是："+calculator.getResult());
        System.out.println("");

        calculator.redo();
        System.out.println("redo第三次，当前计算式是："+calculator.getExpression());
        System.out.println("redo第三次，当前结果是："+calculator.getResult());
        System.out.println("");

        calculator.redo();
        System.out.println("redo第四次，当前计算式是："+calculator.getExpression());
        System.out.println("redo第四次，当前结果是："+calculator.getResult());
        System.out.println("");

        calculator.redo();
        System.out.println("redo第五次，当前计算式是："+calculator.getExpression());
        System.out.println("redo第五次，当前结果是："+calculator.getResult());
    }

    /**
     * 默认除法运算精度
     */
    private static final int DEFAULT_DIV_SCALE = 10;

    /**
     * 当前对象表达式的计算结果
     */
    private BigDecimal result = new BigDecimal("0");
    /**
     * 当前对象的计算表达式
     */
    private String expression = "";

    /**
     * 当前是否为原点状态
     */
    private boolean isOriginStatus = true;

    private LinkedList<Calculator> list = new LinkedList<Calculator>();

    private int index = 0;

    public Calculator() {
    }

    public Calculator(BigDecimal result, String expression) {
        this.result = result;
        this.expression = expression;
    }

    public BigDecimal getResult(){
        if(index == 0 && isOriginStatus){
            return new BigDecimal("0");
        }

        Calculator calculator = list.get(index);
        if(calculator == null){
            return new BigDecimal("0");
        }
        return calculator.result;
    }

    public String getExpression(){
        if(index == 0 && isOriginStatus){
            return "";
        }

        Calculator calculator = list.get(index);
        if(calculator == null){
            return "";
        }
        return calculator.expression;
    }

    public Calculator undo(){
        if(index > 0){
            --index;
        }else if(index == 0){
            if(isOriginStatus){
                throw new IllegalStateException("Unable to roll back again");
            }else{
                isOriginStatus = true;
            }
        }

        return this;
    }

    public Calculator redo(){
        if(index == 0 && isOriginStatus){
            isOriginStatus = false;
        }else{
            if(index == (list.size()-1)){
                throw new IllegalStateException("Unable to recover again");
            }

            ++index;
        }

        return this;
    }



    /**
     * 按照给定的算术运算符做计算
     *
     * @param firstValue  第一个值
     * @param secondValue 第二个值
     * @param currentOp   算数符，只支持'+'、'-'、'*'、'/'
     * @return 结果
     */
    public BigDecimal calculate(String firstValue, String secondValue, char currentOp) {
        BigDecimal result;
        switch (currentOp) {
            case '+':
                result = add(firstValue, secondValue);
                break;
            case '-':
                result = sub(firstValue, secondValue);
                break;
            case '*':
                result = mul(firstValue, secondValue);
                break;
            case '/':
                result = div(firstValue, secondValue);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentOp);
        }
        this.result = result;
        this.expression = firstValue+currentOp+secondValue;

        if(list.size() == 0){
            index = 0;
        }else{
            ++index;
        }

        list.add(new Calculator(result,expression));
        isOriginStatus = false;
        return result;
    }





    /**
     * 提供精确的加法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被加值
     * @return 和
     * @since 4.0.0
     */
    public static BigDecimal add(String... values) {
        if (values == null || values.length == 0) {
            return BigDecimal.ZERO;
        }

        String value = values[0];
        BigDecimal result = toBigDecimal(value);
        for (int i = 1; i < values.length; i++) {
            value = values[i];
            if (value != null && !"".equals(value)) {
                result = result.add(toBigDecimal(value));
            }
        }
        return result;
    }

    /**
     * 提供精确的减法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被减值
     * @return 差
     * @since 4.0.0
     */
    public static BigDecimal sub(String... values) {
        if (values == null || values.length == 0) {
            return BigDecimal.ZERO;
        }

        String value = values[0];
        BigDecimal result = toBigDecimal(value);
        for (int i = 1; i < values.length; i++) {
            value = values[i];
            if (isNotEmpty(value)) {
                result = result.subtract(toBigDecimal(value));
            }
        }
        return result;
    }

    public static boolean isEmpty( Object str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty( Object str) {
        return !isEmpty(str);
    }


    /**
     * 数字转{@link BigDecimal}<br>
     * null或""或空白符转换为0
     *
     * @param number 数字字符串
     * @return {@link BigDecimal}
     * @since 4.0.9
     */
    public static BigDecimal toBigDecimal(String number) {
        try {
            number = parseNumber(number).toString();
        } catch (Exception ignore) {
            // 忽略解析错误
        }
        return isEmpty(number) ? BigDecimal.ZERO : new BigDecimal(number);
    }

    public static boolean isBlank(CharSequence str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return true;
        }

        for (int i = 0; i < length; i++) {
            // 只要有一个非空字符即为非空字符串
            if (false == isBlankChar(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 是否空白符<br>
     * 空白符包括空格、制表符、全角空格和不间断空格<br>
     *
     * @param c 字符
     * @return 是否空白符
     * @see Character#isWhitespace(int)
     * @see Character#isSpaceChar(int)
     * @since 4.0.10
     */
    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c)
                || Character.isSpaceChar(c)
                || c == '\ufeff'
                || c == '\u202a'
                || c == '\u0000';
    }

    /**
     * 是否空白符<br>
     * 空白符包括空格、制表符、全角空格和不间断空格<br>
     *
     * @param c 字符
     * @return 是否空白符
     * @see Character#isWhitespace(int)
     * @see Character#isSpaceChar(int)
     * @since 4.0.10
     */
    public static boolean isBlankChar(char c) {
        return isBlankChar((int) c);
    }



    /**
     * 将指定字符串转换为{@link Number} 对象
     *
     * @param numberStr Number字符串
     * @return Number对象
     * @throws NumberFormatException 包装了{@link ParseException}，当给定的数字字符串无法解析时抛出
     * @since 4.1.15
     */
    public static Number parseNumber(String numberStr) throws NumberFormatException {
        try {
            return NumberFormat.getInstance().parse(numberStr);
        } catch (ParseException e) {
            final NumberFormatException nfe = new NumberFormatException(e.getMessage());
            nfe.initCause(e);
            throw nfe;
        }
    }

    /**
     * 提供精确的乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 积
     * @since 3.0.8
     */
    public static BigDecimal mul(String v1, String v2) {
        return mul(new BigDecimal(v1), new BigDecimal(v2));
    }



    /**
     * 提供精确的乘法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 积
     */
    public static BigDecimal mul(Number v1, Number v2) {
        return mul(new Number[]{v1, v2});
    }

    /**
     * 提供精确的乘法运算<br>
     * 如果传入多个值为null或者空，则返回0
     *
     * @param values 多个被乘值
     * @return 积
     * @since 4.0.0
     */
    public static BigDecimal mul(Number... values) {
        if ((values == null || values.length == 0)|| hasNull(values)) {
            return BigDecimal.ZERO;
        }

        Number value = values[0];
        BigDecimal result = new BigDecimal(value.toString());
        for (int i = 1; i < values.length; i++) {
            value = values[i];
            result = result.multiply(new BigDecimal(value.toString()));
        }
        return result;
    }

    /**
     * 是否包含{@code null}元素
     *
     * @param <T>   数组元素类型
     * @param array 被检查的数组
     * @return 是否包含{@code null}元素
     * @since 3.0.7
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean hasNull(T... array) {
        if (isNotEmpty(array)) {
            for (T element : array) {
                if (null == element) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(float v1, float v2) {
        return div(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(float v1, double v2) {
        return div(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, float v2) {
        return div(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(Double v1, Double v2) {
        return div(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     * @since 3.1.0
     */
    public static BigDecimal div(Number v1, Number v2) {
        return div(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static BigDecimal div(String v1, String v2) {
        return div(v1, v2, DEFAULT_DIV_SCALE);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     */
    public static double div(float v1, float v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     */
    public static double div(float v1, double v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     */
    public static double div(double v1, float v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     */
    public static double div(Double v1, Double v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     * @since 3.1.0
     */
    public static BigDecimal div(Number v1, Number v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确度，如果为负值，取绝对值
     * @return 两个参数的商
     */
    public static BigDecimal div(String v1, String v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     */
    public static double div(float v1, float v2, int scale, RoundingMode roundingMode) {
        return div(Float.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     */
    public static double div(float v1, double v2, int scale, RoundingMode roundingMode) {
        return div(Float.toString(v1), Double.toString(v2), scale, roundingMode).doubleValue();
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     */
    public static double div(double v1, float v2, int scale, RoundingMode roundingMode) {
        return div(Double.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale, RoundingMode roundingMode) {
        return div(Double.toString(v1), Double.toString(v2), scale, roundingMode).doubleValue();
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     */
    public static double div(Double v1, Double v2, int scale, RoundingMode roundingMode) {
        //noinspection RedundantCast
        return div((Number) v1, (Number) v2, scale, roundingMode).doubleValue();
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     * @since 3.1.0
     */
    public static BigDecimal div(Number v1, Number v2, int scale, RoundingMode roundingMode) {
        return div(v1.toString(), v2.toString(), scale, roundingMode);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     */
    public static BigDecimal div(String v1, String v2, int scale, RoundingMode roundingMode) {
        return div(toBigDecimal(v1), toBigDecimal(v2), scale, roundingMode);
    }

    /**
     * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
     *
     * @param v1           被除数
     * @param v2           除数
     * @param scale        精确度，如果为负值，取绝对值
     * @param roundingMode 保留小数的模式 {@link RoundingMode}
     * @return 两个参数的商
     * @since 3.0.9
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
        if(v2 == null){
            throw new RuntimeException("Divisor must be not null !");
        }
        if (null == v1) {
            return BigDecimal.ZERO;
        }
        if (scale < 0) {
            scale = -scale;
        }
        return v1.divide(v2, scale, roundingMode);
    }
}
