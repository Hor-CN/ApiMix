import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.util.Date;

/**
 * @Author: Hor
 * @Date: 2024/6/21 上午10:30
 * @Version: 1.0
 */
public class test {
    public static void main(String[] args) {

//        Date dateTime = DateUtil.parseDate("2024-07-26 17:52:08");
        System.out.println(DateTime.of(1722700800000L));
//        System.out.println(dateTime.getTime());
//        System.out.println(DateUtil.offsetDay(DateUtil.date(), 30));
//        String content = "test中文";
//        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DESede.getValue()).getEncoded();
//        SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DESede, key);
//        //加密
//        byte[] encrypt = des.encrypt(content);
//        //解密
//        byte[] decrypt = des.decrypt(encrypt);
//        //加密为16进制字符串（Hex表示）
//        String encryptHex = des.encryptHex(content);
//        System.out.println("加密：" + encryptHex);
//        //解密为字符串
//        String decryptStr = des.decryptStr(encryptHex);
//        System.out.println("解密：" + decryptStr);
    }
}
