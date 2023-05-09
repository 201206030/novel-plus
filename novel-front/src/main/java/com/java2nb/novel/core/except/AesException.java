package com.java2nb.novel.core.except;

/**
 * @author zhu
 * @date 2023/5/9
 * @description
 */
@SuppressWarnings("serial")
public class AesException extends Exception {

    public final static int OK = 0;
    public final static int ValidateSignatureError = -40001;
    public final static int ParseXmlError = -40002;
    public final static int ComputeSignatureError = -40003;
    public final static int IllegalAesKey = -40004;
    public final static int ValidateAppidError = -40005;
    public final static int EncryptAESError = -40006;
    public final static int DecryptAESError = -40007;
    public final static int IllegalBuffer = -40008;
    public final static int EncodeBase64Error = -40009;
    public final static int DecodeBase64Error = -40010;
    public final static int GenReturnXmlError = -40011;

    private int code;

    private static String getMessage(int code) {
        switch (code) {
            case ValidateSignatureError:
                return "签名验证错误";
            case ParseXmlError:
                return "xml解析失败";
            case ComputeSignatureError:
                return "sha加密生成签名失败";
            case IllegalAesKey:
                return "SymmetricKey非法";
            case ValidateAppidError:
                return "appid校验失败";
            case EncryptAESError:
                return "aes加密失败";
            case DecryptAESError:
                return "aes解密失败";
            case IllegalBuffer:
                return "解密后得到的buffer非法";
            case EncodeBase64Error:
                return "base64加密错误";
            case DecodeBase64Error:
                return "base64解密错误";
            case GenReturnXmlError:
                return "xml生成失败";
            default:
                return null;
        }
    }
    public int getCode() {
        return code;
    }

    public AesException(int code) {
        super(getMessage(code));
        this.code = code;
    }
}
