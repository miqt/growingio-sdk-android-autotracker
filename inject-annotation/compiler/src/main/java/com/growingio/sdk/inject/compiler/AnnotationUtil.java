/*
 * Copyright (C) 2020 Beijing Yishu Technology Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.growingio.sdk.inject.compiler;

import com.sun.tools.javac.code.Attribute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

public final class AnnotationUtil {
    private AnnotationUtil() {
    }

    @Nullable
    public static String getClassValue(ProcessingEnvironment processingEnv, AnnotationMirror annotationMirror, String key) {
        AnnotationValue annotationValue = getAnnotationValue(annotationMirror, key);
        if (annotationValue == null) {
            return null;
        }

        if (annotationValue instanceof Attribute.Class) {
            return ((Attribute.Class) annotationValue).classType.asElement().flatName().toString();
        }
        return null;
        //return getConvertName(processingEnv, annotationValue.getValue().toString());
    }

    public static String getStringValue(AnnotationMirror annotationMirror, String key) {
        AnnotationValue annotationValue = getAnnotationValue(annotationMirror, key);
        if (annotationValue == null) {
            return null;
        }
        return annotationValue.getValue().toString();
    }

    @Nullable
    public static List<String> getClassesValue(AnnotationMirror annotationMirror, String key) {
        AnnotationValue annotationValue = getAnnotationValue(annotationMirror, key);
        if (annotationValue == null) {
            return null;
        }

//        if (annotationValue instanceof Attribute.Array) {
//            Attribute.Array arrayValue = (Attribute.Array) annotationValue;
//            List<String> valueList = new ArrayList<>();
//            for (Attribute value : arrayValue.values) {
//                if (value instanceof Attribute.Class) {
//                    valueList.add(((Attribute.Class) value).classType.asElement().flatName().toString());
//                } else {
//                    throw new IllegalArgumentException(value + " is NOT CLASS");
//                }
//            }
//            return valueList;
//        }

        Object values = annotationValue.getValue();

        if (values instanceof List) {
            List<?> valueList = (List<?>) values;
            List<String> result = new ArrayList<>();
            for (Object current : valueList) {
                result.add(getClassFromAnnotationAttribute(current));
            }
            return result;
        }
        return null;

    }

    private static String getClassFromAnnotationAttribute(Object attribute) {
        if (attribute.getClass().isAssignableFrom(Attribute.UnresolvedClass.class)) {
            throw new IllegalArgumentException(
                    "Failed to parse class for: "
                            + attribute
                            + ".Ensure that all"
                            + "class are included in your classpath.");
        }
        Method[] methods = attribute.getClass().getDeclaredMethods();
        if (methods.length == 0) {
            throw new IllegalArgumentException(
                    "Failed to parse class for: " + attribute);
        }
        for (Method method : methods) {
            if (method.getName().equals("getValue")) {
                try {
                    return method.invoke(attribute).toString();
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalArgumentException("Failed to parse class for: " + attribute, e);
                }
            }
        }
        throw new IllegalArgumentException("Failed to parse class for: " + attribute);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static List<AnnotationMirror> getAnnotations(AnnotationMirror annotationMirror, String key) {
        AnnotationValue annotationValue = getAnnotationValue(annotationMirror, key);
        if (annotationValue == null) {
            return null;
        }

        if (annotationValue.getValue() instanceof List) {
            return (List<AnnotationMirror>) annotationValue.getValue();
        }

        return null;
    }

    public static AnnotationMirror findAnnotationMirror(Element element, Class<?> annotationClazz) {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            if (annotationClazz.getName().equals(annotationMirror.getAnnotationType().toString())) {
                return annotationMirror;
            }
        }
        return null;
    }

    private static AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String key) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> valueMap = annotationMirror.getElementValues();
        for (ExecutableElement executableElement : valueMap.keySet()) {
            if (key.equals(executableElement.getSimpleName().toString())) {
                return valueMap.get(executableElement);
            }
        }
        return null;
    }
}
