package com.app.oktpus.requestModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gyandeep on 1/12/16.
 */

public class CustomDataType {

    private final StringBuilder stringBuilder = new StringBuilder();
    
    public final String encode(Object object) {
        Node root = new Node();
        root.setLevel(0);
        convert(object, object.getClass(), root, 0, false);

        parse(root, "");

        return stringBuilder.substring(0, stringBuilder.length()-1);
    }

    private void parse(Node start, String str) {
        if (start.getChildren().size() == 0) {
            if (start.getValue() instanceof ArrayList) { // for list of primitive data types and not custom ones created for this code
                for (Object o: (List)start.getValue()) {
                    stringBuilder.append(str + "[]=" + o + "&");
                }
            }
            else {
                stringBuilder.append(str + "=" + start.getValue() + "&");
            }
        }
        for (Node child : start.getChildren()) {
            String temp = str;
            if (child.getLevel() == 1)
                temp = child.getLabel();
            else
                temp += "[" + child.getLabel() + "]";
            //Log.d("output", temp);
            parse(child, temp);
            //Log.d("output", child.getLevel() + "-level: " + child.getLabel());
        }

    }

    private void convert(Object obj, Class cls, Node root, int level, boolean parentClass) {
        Field[] fields;
        if (parentClass && cls != null) {
            //Log.d("output", "level: "+ level +", instance: " + cls.toString() + ", " + cls.getSuperclass().toString() + ", " +cls.getDeclaredFields().length);
            fields = cls.getDeclaredFields();
            /*for (Field f : fields) {
                f.setAccessible(true);
                Log.d("output", "field: "+ f.getName());
            }*/
        }
        else if(obj != null && obj.getClass() != null)
            fields = obj.getClass().getDeclaredFields();
        else return;

        if (fields.length > 0) {
            for (Field f : fields) {
                f.setAccessible(true);
                try {
                    if (f.isAnnotationPresent(CustomSerializedName.class) && null != f.get(obj)) {
                        Node node = new Node();
                        node.setLabel(f.getAnnotation(CustomSerializedName.class).value());
                        node.setLevel(level + 1);
                        root.getChildren().add(node);
                        //Log.d("output", "instance: " + f.getName() + ", val: " + f.get(obj));
                        node.setValue(f.get(obj));
                        if (f.get(obj) instanceof ArrayList) {
                            List<Object> list = (ArrayList<Object>) f.get(obj);
                            for (Object o: list) {
                                convert(o, cls, node, level + 1, false);
                            }
                            /*Log.d("output", "before in list level: "+ level +", instance: " + list.get(0).getClass().getSuperclass().toString());
                            convert(f.get(obj), f.get(obj).getClass().getSuperclass(), node, level, true);*/
                        }
                        else {
                            convert(f.get(obj), cls, node, level + 1, false);
                            if(f.get(obj) != null && f.get(obj).getClass() != null) {
                                Class clas  = f.get(obj).getClass().getSuperclass();
                                while (null != clas) {
                                    convert(f.get(obj), clas, node, level, true);
                                    clas = clas.getSuperclass();
                                }
                            }
                        }
                        //Log.d("output", "before level: "+ level +", instance: " + f.get(obj).getClass().toString() + ", "+ f.get(obj).getClass().getSuperclass().toString());

                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Node {
        private String label;
        private int level;
        private List<Node> children = new ArrayList<>();
        private Object value;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public List<Node> getChildren() {
            return children;
        }

        public void setChildren(List<Node> children) {
            this.children = children;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

    }
}
