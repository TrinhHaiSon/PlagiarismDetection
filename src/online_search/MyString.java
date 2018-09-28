/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package online_search;

/**
 *
 * @author trinhhaison
 */
public class MyString {
    
    public static String safeString(String str){
        
        String safe = str.replaceAll("\\\\", "\\\\\\\\");
        safe = safe.replaceAll("\"", "\\\\\"");
        return safe;
    }
    
    public static void main(String[] args) {
//        String a = "this is double quote: \", this is slash: \\";
//        System.out.println(safeCommand(a));
        String a = "luanvan.net.vn/luan-van/de-tai-mo-hinh-cmmcmmi-trong-sqa-76126/";
        String[] b = a.split("/");
//        for(String c : b){
//            System.out.println(c);
//        }
        System.out.println(b[b.length-1]);
    }
    
}
