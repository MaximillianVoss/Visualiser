//package Parse;
//
//import lombok.val;
//import ru.bmstu.processing.models.Block;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.*;
//import java.util.function.Consumer;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import java.util.stream.StreamSupport;
//
//public class ExperimentParser {
//    public void parse(String filePath){
//
//        Map<String, List<Double>> paramsMap = new LinkedHashMap<>(); //создание HashMap для хранения наших параметров
//        List<Block> blocksArray = new ArrayList<>(); //создание List массива для хранения значений парметров полета
//        double firstTime = 0; //начальная точка времени
//
//        BufferedReader reader; //чтение .txt файла
//        try {
//            reader = new BufferedReader(new FileReader(filePath)); //передаем наш файл в BufferedReader
//            String line = reader.readLine(); //записали в строку все параметры полета
//
//            while (line != null) {
//                if (line.equals(">>>>>>>>>>")) {   //закончили сохранение(окончание блока эксперимента)
//                    Block block = new Block(); //создаем новый объект класса блок
//
//                    final double firstTimeConst = firstTime; //константа 0ого времени
//                    List<Double> times = paramsMap.get("Time_SPNM"); // записываем в массив times наш параметр Time_
//                    times = times.stream().map(elem -> {
//                      return round(elem - firstTimeConst, 2 );
//                    }).collect(Collectors.toList());//round
//
//                    paramsMap.replace("Time_SPNM", times);
//
//                    block.setParamsMap(new LinkedHashMap<>(paramsMap));
//
//                    //какой-то вывод
////                    for (val entrySet : paramsMap.entrySet()) {
////                        System.out.println(entrySet.getKey() + "  " + entrySet.getValue());
////                    }
//
//                    paramsMap.clear();
//                    blocksArray.add(block);
//                } else {
//                    try {
//                        Stream<String> stringStream = Arrays.stream(line.trim().split("\\s+"));
//                        stringStream = skipLastElements(stringStream, 1);
//                        String[] columnArray = stringStream
//                                .toArray(String[]::new);
//                        if (columnArray[1].equals("Time_SPNM")){
//                            for (int i = 1; i < columnArray.length-1; i++) {
//                                paramsMap.put(columnArray[i], new ArrayList<>());
//                            }
//                        }
//
//
//                        // TODO: оптимизировать
//                        Stream<String> valuesStream = Arrays.stream(line.trim().split("\\s+"));
//                        valuesStream = skipLastElements(valuesStream, 1);
//                        Double[] tickArray = valuesStream
//                                .map(Double::parseDouble)
//                                .toArray(Double[]::new);
//
//                        if (tickArray[0].equals(1.0)) {
//                            firstTime = tickArray[1];
//                            int i = 1;
//                            for (val entrySet : paramsMap.entrySet()) {
//                                entrySet.getValue().add(tickArray[i]);
//                                i++;
//                            }
////                            timeArray.add(0.0);
////                            xrKArray.add(tickArray[2]);
//                        } else {
//                            int i = 1;
//                            for (val entrySet : paramsMap.entrySet()) {
//                                entrySet.getValue().add(tickArray[i]);
//                                i++;
//                            }
////                            timeArray.add(round(timeArray.get(timeArray.size() - 1) + 0.02, 2));
////                            xrKArray.add(tickArray[2]);
//                        }
//
//                    } catch (Exception e) {
//
//                    }
//                }
//                line = reader.readLine();
//            }
//
//            reader.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        for (Block block : blocksArray) {
//            System.out.println(block);
//        }
////
//        for (val entrySet : blocksArray.get(0).getParamsMap().entrySet()) {
//            System.out.println(entrySet.getKey() + "  " + entrySet.getValue());
//        }
//        blocksArray.get(0).getParamsMap().get("Time_SPNM").forEach(System.out::println);
////        System.out.println(blocksArray.get(0).getParamsMap().get("Time_SPNM").get(0));
//
//    }
//
//
////Пропуск последнего параметра полета(HH:MM:SS:MS)
//
//    static <T> Stream<T> skipLastElements(Stream<T> s, int count) {
//        if(count<=0) {
//            if(count==0) return s;
//            throw new IllegalArgumentException(count+" < 0");
//        }
//        ArrayDeque<T> pending=new ArrayDeque<T>(count+1);
//        Spliterator<T> src=s.spliterator();
//        return StreamSupport.stream(new Spliterator<T>() {
//            public boolean tryAdvance(Consumer<? super T> action) {
//                while(pending.size()<=count && src.tryAdvance(pending::add));
//                if(pending.size()>count) {
//                    action.accept(pending.remove());
//                    return true;
//                }
//                return false;
//            }
//            public Spliterator<T> trySplit() {
//                return null;
//            }
//            public long estimateSize() {
//                return src.estimateSize()-count;
//            }
//            public int characteristics() {
//                return src.characteristics();
//            }
//        }, false);
//    }
//
//    public static double round(double value, int places) {
//        if (places < 0) throw new IllegalArgumentException();
//        long factor = (long) Math.pow(10, places);
//        value = value * factor;
//        long tmp = Math.round(value);
//        return (double) tmp / factor;
//    }
//}