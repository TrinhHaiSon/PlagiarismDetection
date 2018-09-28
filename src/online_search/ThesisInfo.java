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
public class ThesisInfo {
    private String link;
        private String name;
        
        public ThesisInfo(String link) {
            this.link = link;
            String[] parts = link.split("/");
            this.name = parts[parts.length - 1];
        }

        public String getLink() {
            return link;
        }

        public String getName() {
            return name;
        }
}
