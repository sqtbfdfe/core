 package org.liuz.core.utils;

 import net.sourceforge.pinyin4j.PinyinHelper;
 import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
 import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
 import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
 import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
 import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

 public class PinYinUtil
 {
   public static String cn2FirstSpell(String chinese)
   {
     StringBuffer pybf = new StringBuffer();
     char[] arr = chinese.toCharArray();
     HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
     defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
     defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
     for (int i = 0; i < arr.length; i++) {
       if (arr[i] > '')
         try {
           String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
           if (_t == null) continue;
           pybf.append(_t[0].charAt(0));
         }
         catch (BadHanyuPinyinOutputFormatCombination e) {
           e.printStackTrace();
         }
       else {
         pybf.append(arr[i]);
       }
     }
     return pybf.toString().replaceAll("\\W", "").trim();
   }

   public static String cn2Spell(String chinese)
   {
     StringBuffer pybf = new StringBuffer();
     char[] arr = chinese.toCharArray();
     HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
     defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
     defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
     for (int i = 0; i < arr.length; i++) {
       if (arr[i] > '')
         try {
           pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
         } catch (BadHanyuPinyinOutputFormatCombination e) {
           e.printStackTrace();
         }
       else {
         pybf.append(arr[i]);
       }
     }
     return pybf.toString();
   }

   public static String toPinYin(String hanzhis) {
     CharSequence s = hanzhis;

     char[] hanzhi = new char[s.length()];
     for (int i = 0; i < s.length(); i++) {
       hanzhi[i] = s.charAt(i);
     }

     char[] t1 = hanzhi;
     String[] t2 = new String[s.length()];

     HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
     t3.setCaseType(HanyuPinyinCaseType.UPPERCASE);
     t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
     t3.setVCharType(HanyuPinyinVCharType.WITH_V);

     int t0 = t1.length;
     String py = "";
     try {
       for (int i = 0; i < t0; i++) {
         t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
         py = py + t2[0].toString();
       }
     } catch (BadHanyuPinyinOutputFormatCombination e1) {
       e1.printStackTrace();
     }

     return py.trim();
   }
 }
