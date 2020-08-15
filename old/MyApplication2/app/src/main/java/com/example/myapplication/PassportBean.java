package com.example.myapplication;

class PassportBean {

    /**
     * log_id : 3794191331807948071
     * words_result_num : 11
     * words_result : {"国家码":{"location":{"width":40,"top":504,"left":426,"height":17},"words":"CHN"},"护照签发地点":{"location":{"width":116,"top":686,"left":337,"height":18},"words":"深圳/SHENZHEN"},"有效期至":{"location":{"width":133,"top":686,"left":532,"height":17},"words":"20250209"},"签发机关":{"location":{"width":131,"top":726,"left":339,"height":17},"words":"公安部出入境管理局"},"护照号码":{"location":{"width":132,"top":507,"left":556,"height":22},"words":"E12345678"},"签发日期":{"location":{"width":134,"top":645,"left":533,"height":17},"words":"20150208"},"出生地点":{"location":{"width":116,"top":646,"left":338,"height":18},"words":"深圳/SHENZHEN"},"姓名":{"location":{"width":56,"top":541,"left":338,"height":21},"words":"艾米"},"姓名拼音":{"location":{"width":60,"top":561,"left":337,"height":17},"words":"AI,MI"},"生日":{"location":{"width":105,"top":606,"left":532,"height":17},"words":"19901222"},"性别":{"location":{"width":36,"top":607,"left":338,"height":16},"words":"女/F"}}
     */

    private long log_id;
    private int words_result_num;
    private WordsResultBean words_result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public WordsResultBean getWords_result() {
        return words_result;
    }

    public void setWords_result(WordsResultBean words_result) {
        this.words_result = words_result;
    }

    public static class WordsResultBean {
        /**
         * 国家码 : {"location":{"width":40,"top":504,"left":426,"height":17},"words":"CHN"}
         * 护照签发地点 : {"location":{"width":116,"top":686,"left":337,"height":18},"words":"深圳/SHENZHEN"}
         * 有效期至 : {"location":{"width":133,"top":686,"left":532,"height":17},"words":"20250209"}
         * 签发机关 : {"location":{"width":131,"top":726,"left":339,"height":17},"words":"公安部出入境管理局"}
         * 护照号码 : {"location":{"width":132,"top":507,"left":556,"height":22},"words":"E12345678"}
         * 签发日期 : {"location":{"width":134,"top":645,"left":533,"height":17},"words":"20150208"}
         * 出生地点 : {"location":{"width":116,"top":646,"left":338,"height":18},"words":"深圳/SHENZHEN"}
         * 姓名 : {"location":{"width":56,"top":541,"left":338,"height":21},"words":"艾米"}
         * 姓名拼音 : {"location":{"width":60,"top":561,"left":337,"height":17},"words":"AI,MI"}
         * 生日 : {"location":{"width":105,"top":606,"left":532,"height":17},"words":"19901222"}
         * 性别 : {"location":{"width":36,"top":607,"left":338,"height":16},"words":"女/F"}
         */

        private 国家码Bean 国家码;
        private 护照签发地点Bean 护照签发地点;
        private 有效期至Bean 有效期至;
        private 签发机关Bean 签发机关;
        private 护照号码Bean 护照号码;
        private 签发日期Bean 签发日期;
        private 出生地点Bean 出生地点;
        private 姓名Bean 姓名;
        private 姓名拼音Bean 姓名拼音;
        private 生日Bean 生日;
        private 性别Bean 性别;

        public 国家码Bean get国家码() {
            return 国家码;
        }

        public void set国家码(国家码Bean 国家码) {
            this.国家码 = 国家码;
        }

        public 护照签发地点Bean get护照签发地点() {
            return 护照签发地点;
        }

        public void set护照签发地点(护照签发地点Bean 护照签发地点) {
            this.护照签发地点 = 护照签发地点;
        }

        public 有效期至Bean get有效期至() {
            return 有效期至;
        }

        public void set有效期至(有效期至Bean 有效期至) {
            this.有效期至 = 有效期至;
        }

        public 签发机关Bean get签发机关() {
            return 签发机关;
        }

        public void set签发机关(签发机关Bean 签发机关) {
            this.签发机关 = 签发机关;
        }

        public 护照号码Bean get护照号码() {
            return 护照号码;
        }

        public void set护照号码(护照号码Bean 护照号码) {
            this.护照号码 = 护照号码;
        }

        public 签发日期Bean get签发日期() {
            return 签发日期;
        }

        public void set签发日期(签发日期Bean 签发日期) {
            this.签发日期 = 签发日期;
        }

        public 出生地点Bean get出生地点() {
            return 出生地点;
        }

        public void set出生地点(出生地点Bean 出生地点) {
            this.出生地点 = 出生地点;
        }

        public 姓名Bean get姓名() {
            return 姓名;
        }

        public void set姓名(姓名Bean 姓名) {
            this.姓名 = 姓名;
        }

        public 姓名拼音Bean get姓名拼音() {
            return 姓名拼音;
        }

        public void set姓名拼音(姓名拼音Bean 姓名拼音) {
            this.姓名拼音 = 姓名拼音;
        }

        public 生日Bean get生日() {
            return 生日;
        }

        public void set生日(生日Bean 生日) {
            this.生日 = 生日;
        }

        public 性别Bean get性别() {
            return 性别;
        }

        public void set性别(性别Bean 性别) {
            this.性别 = 性别;
        }

        public static class 国家码Bean {
            /**
             * location : {"width":40,"top":504,"left":426,"height":17}
             * words : CHN
             */

            private LocationBean location;
            private String words;

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBean {
                /**
                 * width : 40
                 * top : 504
                 * left : 426
                 * height : 17
                 */

                private int width;
                private int top;
                private int left;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }

        public static class 护照签发地点Bean {
            /**
             * location : {"width":116,"top":686,"left":337,"height":18}
             * words : 深圳/SHENZHEN
             */

            private LocationBeanX location;
            private String words;

            public LocationBeanX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanX {
                /**
                 * width : 116
                 * top : 686
                 * left : 337
                 * height : 18
                 */

                private int width;
                private int top;
                private int left;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }

        public static class 有效期至Bean {
            /**
             * location : {"width":133,"top":686,"left":532,"height":17}
             * words : 20250209
             */

            private LocationBeanXX location;
            private String words;

            public LocationBeanXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXX {
                /**
                 * width : 133
                 * top : 686
                 * left : 532
                 * height : 17
                 */

                private int width;
                private int top;
                private int left;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }

        public static class 签发机关Bean {
            /**
             * location : {"width":131,"top":726,"left":339,"height":17}
             * words : 公安部出入境管理局
             */

            private LocationBeanXXX location;
            private String words;

            public LocationBeanXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXX {
                /**
                 * width : 131
                 * top : 726
                 * left : 339
                 * height : 17
                 */

                private int width;
                private int top;
                private int left;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }

        public static class 护照号码Bean {
            /**
             * location : {"width":132,"top":507,"left":556,"height":22}
             * words : E12345678
             */

            private LocationBeanXXXX location;
            private String words;

            public LocationBeanXXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXXX {
                /**
                 * width : 132
                 * top : 507
                 * left : 556
                 * height : 22
                 */

                private int width;
                private int top;
                private int left;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }

        public static class 签发日期Bean {
            /**
             * location : {"width":134,"top":645,"left":533,"height":17}
             * words : 20150208
             */

            private LocationBeanXXXXX location;
            private String words;

            public LocationBeanXXXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXXXX {
                /**
                 * width : 134
                 * top : 645
                 * left : 533
                 * height : 17
                 */

                private int width;
                private int top;
                private int left;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }

        public static class 出生地点Bean {
            /**
             * location : {"width":116,"top":646,"left":338,"height":18}
             * words : 深圳/SHENZHEN
             */

            private LocationBeanXXXXXX location;
            private String words;

            public LocationBeanXXXXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXXXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXXXXX {
                /**
                 * width : 116
                 * top : 646
                 * left : 338
                 * height : 18
                 */

                private int width;
                private int top;
                private int left;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }

        public static class 姓名Bean {
            /**
             * location : {"width":56,"top":541,"left":338,"height":21}
             * words : 艾米
             */

            private LocationBeanXXXXXXX location;
            private String words;

            public LocationBeanXXXXXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXXXXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXXXXXX {
                /**
                 * width : 56
                 * top : 541
                 * left : 338
                 * height : 21
                 */

                private int width;
                private int top;
                private int left;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }

        public static class 姓名拼音Bean {
            /**
             * location : {"width":60,"top":561,"left":337,"height":17}
             * words : AI,MI
             */

            private LocationBeanXXXXXXXX location;
            private String words;

            public LocationBeanXXXXXXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXXXXXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXXXXXXX {
                /**
                 * width : 60
                 * top : 561
                 * left : 337
                 * height : 17
                 */

                private int width;
                private int top;
                private int left;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }

        public static class 生日Bean {
            /**
             * location : {"width":105,"top":606,"left":532,"height":17}
             * words : 19901222
             */

            private LocationBeanXXXXXXXXX location;
            private String words;

            public LocationBeanXXXXXXXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXXXXXXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXXXXXXXX {
                /**
                 * width : 105
                 * top : 606
                 * left : 532
                 * height : 17
                 */

                private int width;
                private int top;
                private int left;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }

        public static class 性别Bean {
            /**
             * location : {"width":36,"top":607,"left":338,"height":16}
             * words : 女/F
             */

            private LocationBeanXXXXXXXXXX location;
            private String words;

            public LocationBeanXXXXXXXXXX getLocation() {
                return location;
            }

            public void setLocation(LocationBeanXXXXXXXXXX location) {
                this.location = location;
            }

            public String getWords() {
                return words;
            }

            public void setWords(String words) {
                this.words = words;
            }

            public static class LocationBeanXXXXXXXXXX {
                /**
                 * width : 36
                 * top : 607
                 * left : 338
                 * height : 16
                 */

                private int width;
                private int top;
                private int left;
                private int height;

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getTop() {
                    return top;
                }

                public void setTop(int top) {
                    this.top = top;
                }

                public int getLeft() {
                    return left;
                }

                public void setLeft(int left) {
                    this.left = left;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }
    }
}
