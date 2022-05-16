package com.denisov.onbalance.email;

public class ConfirmationTokenEmail {
    private String constructEmail(String confirmationCode){
        String email = "<html>\n" +
                "    <head>\n" +
                "        <link href=\"http://fonts.cdnfonts.com/css/peace-sans\" rel=\"stylesheet\">\n" +
                "        <link href=\"http://fonts.cdnfonts.com/css/jura\" rel=\"stylesheet\">\n" +
                "        <style>\n" +
                "            #div1 {position: relative; margin: auto; width: 100%; height: 100%;}\n" +
                "            #div2 {width:670px; height: 384px; background-color:#F3AE30; margin:auto; margin-top:50px; }\n" +
                "            #div3 {height:20px}\n" +
                "            #img1 {display: block; margin-left: 260px; margin-top: 50px;}\n" +
                "            #textblock1 {font-family: 'Peace Sans', sans-serif; text-align: center; font-size: 40px; margin-top: 20px}\n" +
                "            #textblock2 {font-family: 'jura', sans-serif; font-size:10px; text-align: center; margin-top:-20px }\n" +
                "          </style>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div id=\"div1\">\n" +
                "            <div id=\"div2\">\n" +
                "                    <div id=\"div3\"></div>\n" +
                "                    <img src=\"fridge2.png\" alt=\"Anything\" width=\"150px\" id=\"img1\">\n" +
                "                    <h2 id=\"textblock1\">BFDS6</h2>\n" +
                "                    <h2 id=\"textblock2\">Expires in 5 minutes</h2>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </body>\n" +
                "</html>";
        return email;
    }
}
