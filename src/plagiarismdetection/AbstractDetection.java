/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package plagiarismdetection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import static plagiarismdetection.PlagiarismDetection.POS;
import static plagiarismdetection.PlagiarismDetection.readDocFile;
import static plagiarismdetection.PlagiarismDetection.readDocxFile;
import vn.pipeline.Word;

/**
 *
 * @author trinhhaison
 */
public class AbstractDetection {
    String MucLuc_text1 = "(|  |\n *)(MỤC LỤC|CONTENTS|TABLE OF CONTENTS|MỤC LỤC) *\n";
    String MucLuc_text2 = "(|  |\n *)(Mục Lục|Contents|Table Of Contents|Mục Lục) *\n";
    String MucLuc_text3 = "(|  |\n *)(Mục lục|Contents|Table of contents|Mục lục) *\n";

    String TomTat_text1 = "(|  |\n *)(TÓM TẮT NỘI DUNG ĐỒ ÁN TỐT NGHIỆP|TÓM TẮT ĐỒ ÁN|TÓM TẮT NỘI DUNG ĐỒ ÁN:?|TÓM TẮT|TÓM TẮT NỘI DUNG ĐỒ ÁN TỐT NGHIỆP) *\n";
    String TomTat_text2 = "(|  |\n *)(Tóm Tắt Nội Dung Đồ Án Tốt Nghiệp|Tóm Tắt Đồ Án|Tóm Tắt Nội Dung Đồ Án:?|Tóm Tắt|Tóm Tắt Nội Dung Đồ Án Tốt Nghiệp) *\n";
    String TomTat_text3 = "(|  |\n *)(Tóm tắt nội dung đồ án tốt nghiệp|Tóm tắt đồ án|Tóm tắt nội dung đồ án:?|Tóm tắt nội dung đồ án tốt nghiệp) *\n";

    String MoDau_text1 = "(|  |\n *)((LỜI|PHẦN) (MỞ|NÓI) ĐẦU *\n|MỞ ĐẦU *\n)";
    String MoDau_text2 = "(|  |\n *)((Lời|Phần) (Mở|Nói) Đầu *\n|Mở Đầu *\n)";
    String MoDau_text3 = "(|  |\n *)((Lời|Phần) (mở|nói) đầu *\n|Mở đầu *\n)";

    String KetThuc_text1 = "(|  |\n *)(MỤC LỤC|CONTENTS|TABLE OF CONTENTS|MỤC LỤC|LỜI CẢM ƠN|(LỜI|PHẦN) (MỞ|NÓI) ĐẦU|MỞ ĐẦU|BẢNG PHÂN CÔNG CÔNG VIỆC|ABSTRACT OF THESIS|GIỚI THIỆU CHUNG|DANH MỤC.*|PHIẾU GIAO.*|ABSTRACT|LỜI C[ẢÁ]M ƠN|LỜI CÁM ƠN|(CHƯƠNG|PHẦN) (1|MỘT|I)) *\n|(|  |\n *)(CHƯƠNG|PHẦN) (1[^0-9]|MỘT|I[^I]).*\n";
    String KetThuc_text2 = "(|  |\n *)(Mục Lục|Contents|Table Of Contents|Mục Lục|Lời Cảm Ơn|(Lời|Phần) (Mở|Nói) Đầu|Mở Đầu|Bảng Phân Công Công Việc|Abstract Of Thesis|Giới Thiệu Chung|Danh Mục.*|Phiếu Giao.*|Abstract|Lời C[ảá]m Ơn|Lời Cám Ơn|(Chương|Phần) (1|Một|i)) *\n|(|  |\n *)(Chương|Phần) (1[^0-9]|Một|I[^I]).*\n";
    String KetThuc_text3 = "(|  |\n *)(Mục lục|Contents|Table of contents|Mục lục|Lời cảm ơn|(Lời|Phần) (mở|nói) đầu|Mở đầu|Bảng phân công công việc|Abstract of thesis|Giới thiệu chung|Danh mục.*|phiếu giao.*|Abstract|Lời c[ảá]m ơn|Lời cám ơn|(Chương|Phần) (1|một|i)) *\n|(|  |\n *)(Chương|Phần) (1[^0-9]|một|i[^i]).*\n";

    String Chuong1_text1 = "(|  |\n *)(CHƯƠNG|PHẦN) (1|MỘT|I) *\n|(|  |\n *)(CHƯƠNG|PHẦN) (1[^0-9]|MỘT|I[^I]).*\n";
    String Chuong1_text2 = "(|  |\n *)(Chương|Phần) (1|Một|I) *\n";
    String Chuong1_text3 = "(|  |\n *)(Chương|Phần) (1[^0-9]|một|I[^I]).*\n";
    String Chuong1_text4 = "(|  |\n *)(1[^0-9]|I[^I]).*\n";

    String Chuong2_text1 = "(|  |\n *)(CHƯƠNG|PHẦN) (2|HAI|II) *\n|(|  |\n *)(CHƯƠNG|PHẦN) (2[^0-9]|HAI|II[^I]).*\n";
    String Chuong2_text2 = "(|  |\n *)(Chương|Phần) (2|Hai|II) *\n";
    String Chuong2_text3 = "(|  |\n *)(Chương|Phần) (2[^0-9]|hai|II[^I]).*\n";
    String Chuong2_text4 = "(|  |\n *)(2[^0-9]|II[^I]).*\n";
    
    List<String> TomTat_text;
    List<String> MoDau_text;
    List<String> Chuong1_text;
    List<String> Chuong2_text;
    List<String> KetThuc_text;
    List<String> MucLuc_text;

    public AbstractDetection() {
        TomTat_text = new ArrayList<>();
        TomTat_text.add(TomTat_text1);
        TomTat_text.add(TomTat_text2);
        TomTat_text.add(TomTat_text3);
        
        MoDau_text = new ArrayList<>();
        MoDau_text.add(MoDau_text1);
        MoDau_text.add(MoDau_text2);
        MoDau_text.add(MoDau_text3);
        
        Chuong1_text = new ArrayList<>();
        Chuong1_text.add(Chuong1_text1);
        Chuong1_text.add(Chuong1_text2);
        Chuong1_text.add(Chuong1_text3);
        Chuong1_text.add(Chuong1_text4);
        
        Chuong2_text = new ArrayList<>();
        Chuong2_text.add(Chuong2_text1);
        Chuong2_text.add(Chuong2_text2);
        Chuong2_text.add(Chuong2_text3);
        Chuong2_text.add(Chuong2_text4);
        
        KetThuc_text = new ArrayList<>();
        KetThuc_text.add(KetThuc_text1);
        KetThuc_text.add(KetThuc_text2);
        KetThuc_text.add(KetThuc_text3);
        
        MucLuc_text = new ArrayList<>();
        MucLuc_text.add(MucLuc_text1);
        MucLuc_text.add(MucLuc_text2);
        MucLuc_text.add(MucLuc_text3);
    }
    
    public String nomalize(String text){
        text = text.replaceAll("[sS]inh viên thực hiện.*\n", " ");
        text = text.replaceAll("\n+", "\n");
        return text;
    }
    
    public  Matcher search(List<String> list, String text){
        Pattern p;
        Matcher m;
        for(String regex : list){
            p = Pattern.compile(regex);
            m = p.matcher(text);
            if(m.find() == true){
                return m;
            }
        }
        return null;
    }
    
    public String getAbstract(String text){
        Matcher MucLuc = search(MucLuc_text, text);
        String TruocMucLuc;
        String SauMucLuc;
        Matcher TomTat;
        Matcher TomTat_;
        Matcher TomTat__;
        Matcher MoDau;
        Matcher MoDau_;
        Matcher MoDau__;
        Matcher KetThucTomTat;
        Matcher KetThucMoDau;
        Matcher Chuong1_;
        Matcher d;
        Matcher n;
        Matcher m;
        if(MucLuc != null){
            TruocMucLuc = text.substring(0, MucLuc.end());
            SauMucLuc = text.substring(MucLuc.end());
            TomTat = search(TomTat_text, TruocMucLuc);
            MoDau = search(MoDau_text, TruocMucLuc);
            if(TomTat != null){
                KetThucTomTat = search(KetThuc_text, TruocMucLuc.substring(TomTat.end()));
                return TruocMucLuc.substring(TomTat.end(), TomTat.end() + KetThucTomTat.start());
            }
            else if(MoDau != null){
                KetThucMoDau = search(KetThuc_text, TruocMucLuc.substring(MoDau.end()));
                return TruocMucLuc.substring(MoDau.end(), MoDau.end() + KetThucMoDau.start());
            }
            else{
                TomTat_ = search(TomTat_text, SauMucLuc);
                MoDau_ = search(MoDau_text, SauMucLuc);
                Chuong1_ = search(Chuong1_text, SauMucLuc);
                if(TomTat_ != null){
                    try{
                        TomTat__ = search(TomTat_text, SauMucLuc.substring(TomTat_.end()));
                        KetThucTomTat = search(KetThuc_text, SauMucLuc.substring(TomTat_.end() + TomTat__.end()));
                        return SauMucLuc.substring(TomTat_.end() + TomTat__.end(), TomTat_.end() + TomTat__.end() + KetThucTomTat.start());
                    }
                    catch(Exception e){
                        KetThucTomTat = search(KetThuc_text, SauMucLuc.substring(TomTat_.end()));
                        return SauMucLuc.substring(TomTat_.end(), TomTat_.end() + KetThucTomTat.start());
                    }
                }
                else if (MoDau_ != null){
                    try{
                        MoDau__ = search(MoDau_text, SauMucLuc.substring(MoDau_.end()));
                        KetThucMoDau = search(KetThuc_text, SauMucLuc.substring(MoDau_.end() + MoDau__.end()));
                        return SauMucLuc.substring(MoDau_.end() + MoDau__.end(),MoDau_.end() + MoDau__.end() + KetThucMoDau.start());
                    }
                    catch(Exception e){
                        KetThucMoDau = search(KetThuc_text, SauMucLuc.substring(MoDau_.end()));
                        return SauMucLuc.substring(MoDau_.end(), MoDau_.end() + KetThucMoDau.start());
                    }    
                }
                else if(Chuong1_ != null){
                    d = search(Chuong2_text, SauMucLuc);
                    try{
                        m = search(Chuong1_text, SauMucLuc.substring(d.end()));
                        n = search(Chuong2_text, SauMucLuc.substring(d.end() + m.end()));
                        return SauMucLuc.substring(d.end() + m.end(), d.end() + m.end() + n.start());
                    }
                    catch(Exception e){
                        return SauMucLuc.substring(Chuong1_.end(), d.start());
                    }
                }
                else return null;
            }
        }
        else{
            TomTat_ = search(TomTat_text, text);
            MoDau_ = search(MoDau_text, text);
            Chuong1_ = search(Chuong1_text, text);
            if(TomTat_ != null){
                try{
                    TomTat__ = search(TomTat_text, text.substring(TomTat_.end()));
                    KetThucTomTat = search(KetThuc_text, text.substring(TomTat_.end() + TomTat__.end()));
                    return text.substring(TomTat_.end() + TomTat__.end(), TomTat_.end() + TomTat__.end() + KetThucTomTat.start());
                }
                catch(Exception e){
                    KetThucTomTat = search(KetThuc_text, text.substring(TomTat_.end()));
                    return text.substring(TomTat_.end(), TomTat_.end() + KetThucTomTat.start());
                }
            }
            else if(MoDau_ != null){
                try{
                    MoDau__ = search(MoDau_text, text.substring(MoDau_.end()));
                    KetThucMoDau = search(KetThuc_text, text.substring(MoDau_.end() + MoDau__.end()));
                    return text.substring(MoDau_.end() + MoDau__.end(), MoDau_.end() + MoDau__.end() + KetThucMoDau.start());
                }
                catch(Exception e){
                    KetThucMoDau = search(KetThuc_text, text.substring(MoDau_.end()));
                    return text.substring(MoDau_.end(), MoDau_.end() + KetThucMoDau.start());
                }
            }
            else if(Chuong1_ != null){
                d = search(Chuong2_text, text);
                try{
                    m = search(Chuong1_text, text.substring(d.end()));
                    n = search(Chuong2_text, text.substring(d.end() + m.end()));
                    return text.substring(d.end() + m.end(), d.end() + m.end() + n.start());
                }
                catch(Exception e){
                    return text.substring(Chuong1_.end(), d.start());
                }
            }
        }
        return "";
    }
    
    public static void main(String[] args) throws IOException, FileNotFoundException, InvalidFormatException {
//        String a = "Tôi là Sơn";
//        Pattern p = Pattern.compile("Sơn");
//        Matcher m = p.matcher(a);
//        System.out.println(m.find());
//        System.out.println(m.find());
//        System.out.println(m.end());

        String fileName = "/media/trinhhaison/01D22CD594D997B0/School/NLP/LVTN/67.docx";
        String extension = PlagiarismDetection.getFileExtension(fileName);
        System.out.println(extension);
        String text;
        if(extension.equals("doc")){
            text = readDocFile(fileName);
        }
        else if(extension.equals("docx")){
            text = readDocxFile(fileName);
        }
        else{
            System.out.println("wrong extension");
            return;
        }
        
        AbstractDetection ad = new AbstractDetection();
        text = ad.nomalize(text);
        String abs = ad.getAbstract(text);
        String[] paragraphs = abs.split("\n");
        for (String para : paragraphs){
            System.out.println("/////////////////");
            System.out.println(para);
        }
//        List<Word> wordList = POS(abs);
//        String temp;
//        for(Word word : wordList){
//            temp = word.getForm().replaceAll("_", " ");
//            System.out.println(temp + ": " + word.getPosTag());
//        }
//        System.out.println(abs);
    }
}
