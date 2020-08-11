package com.example.viewmodeldemo.Bean;

public class IdcardResult {

    /**
     * log_id : 7307342730717382429
     * words_result_num : 6
     * image_status : normal
     * words_result : {"住址":{"location":{"width":355,"top":1248,"left":1192,"height":1579},"words":"广东省东源县黄田镇黄坑村委会大屋小组1号"},"出生":{"location":{"width":139,"top":1253,"left":1734,"height":1266},"words":"19980119"},"姓名":{"location":{"width":162,"top":1275,"left":2335,"height":506},"words":"李佳安"},"公民身份号码":{"location":{"width":213,"top":1858,"left":567,"height":2083},"words":"441625199801196415"},"性别":{"location":{"width":130,"top":1257,"left":2036,"height":114},"words":"男"},"民族":{"location":{"width":110,"top":2056,"left":2034,"height":103},"words":"汉"}}
     * idcard_number_type : 1
     */

    private long log_id;
    private int words_result_num;
    private String image_status;
    private WordsResultBean words_result;
    private int idcard_number_type;

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

    public String getImage_status() {
        return image_status;
    }

    public void setImage_status(String image_status) {
        this.image_status = image_status;
    }

    public WordsResultBean getWords_result() {
        return words_result;
    }

    public void setWords_result(WordsResultBean words_result) {
        this.words_result = words_result;
    }

    public int getIdcard_number_type() {
        return idcard_number_type;
    }

    public void setIdcard_number_type(int idcard_number_type) {
        this.idcard_number_type = idcard_number_type;
    }

    public static class WordsResultBean {
        /**
         * 住址 : {"location":{"width":355,"top":1248,"left":1192,"height":1579},"words":"广东省东源县黄田镇黄坑村委会大屋小组1号"}
         * 出生 : {"location":{"width":139,"top":1253,"left":1734,"height":1266},"words":"19980119"}
         * 姓名 : {"location":{"width":162,"top":1275,"left":2335,"height":506},"words":"李佳安"}
         * 公民身份号码 : {"location":{"width":213,"top":1858,"left":567,"height":2083},"words":"441625199801196415"}
         * 性别 : {"location":{"width":130,"top":1257,"left":2036,"height":114},"words":"男"}
         * 民族 : {"location":{"width":110,"top":2056,"left":2034,"height":103},"words":"汉"}
         */

        private 住址Bean 住址;
        private 出生Bean 出生;
        private 姓名Bean 姓名;
        private 公民身份号码Bean 公民身份号码;
        private 性别Bean 性别;
        private 民族Bean 民族;

        public 住址Bean get住址() {
            return 住址;
        }

        public void set住址(住址Bean 住址) {
            this.住址 = 住址;
        }

        public 出生Bean get出生() {
            return 出生;
        }

        public void set出生(出生Bean 出生) {
            this.出生 = 出生;
        }

        public 姓名Bean get姓名() {
            return 姓名;
        }

        public void set姓名(姓名Bean 姓名) {
            this.姓名 = 姓名;
        }

        public 公民身份号码Bean get公民身份号码() {
            return 公民身份号码;
        }

        public void set公民身份号码(公民身份号码Bean 公民身份号码) {
            this.公民身份号码 = 公民身份号码;
        }

        public 性别Bean get性别() {
            return 性别;
        }

        public void set性别(性别Bean 性别) {
            this.性别 = 性别;
        }

        public 民族Bean get民族() {
            return 民族;
        }

        public void set民族(民族Bean 民族) {
            this.民族 = 民族;
        }

        public static class 住址Bean {
            /**
             * location : {"width":355,"top":1248,"left":1192,"height":1579}
             * words : 广东省东源县黄田镇黄坑村委会大屋小组1号
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
                 * width : 355
                 * top : 1248
                 * left : 1192
                 * height : 1579
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

        public static class 出生Bean {
            /**
             * location : {"width":139,"top":1253,"left":1734,"height":1266}
             * words : 19980119
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
                 * width : 139
                 * top : 1253
                 * left : 1734
                 * height : 1266
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
             * location : {"width":162,"top":1275,"left":2335,"height":506}
             * words : 李佳安
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
                 * width : 162
                 * top : 1275
                 * left : 2335
                 * height : 506
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

        public static class 公民身份号码Bean {
            /**
             * location : {"width":213,"top":1858,"left":567,"height":2083}
             * words : 441625199801196415
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
                 * width : 213
                 * top : 1858
                 * left : 567
                 * height : 2083
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
             * location : {"width":130,"top":1257,"left":2036,"height":114}
             * words : 男
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
                 * width : 130
                 * top : 1257
                 * left : 2036
                 * height : 114
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

        public static class 民族Bean {
            /**
             * location : {"width":110,"top":2056,"left":2034,"height":103}
             * words : 汉
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
                 * width : 110
                 * top : 2056
                 * left : 2034
                 * height : 103
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
