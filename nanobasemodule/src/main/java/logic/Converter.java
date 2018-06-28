package logic;

public class Converter {

    //метод конвертирует название запроса в приложении в имя представления в БД
    public static String convertToViewName(String viewNameRussian){
        switch (viewNameRussian){
            case "Свойства керамических нанокомпозитов":{
                return "properties_view";
            }
            case "Статьи по керамическим нанокомпозитам":{
                return "articles";
            }
            case "Область применения керамических нанокомпозитов":{
                return "application_area";
            }
            case "Способы получения керамических нанокомпозитов":{
                return "synthesis_description";
            }
            default: throw new RuntimeException();
        }
    }

    //метод заменяет запятую на точку в числе для возможности его интерпретации
    public static String convertToDoubleParsable(String doubleNumber){
        StringBuilder doubleParsable = new StringBuilder();
        char symbol;
        for (int i = 0; i < doubleNumber.length(); i++) {
            symbol = doubleNumber.charAt(i);
            if(symbol == ','){
                doubleParsable.append('.');
            }
            else {
                doubleParsable.append(symbol);
            }
        }
        return doubleParsable.toString();
    }

    //метод конвертирует химическую формулу из формы для БД в форму для отображения
    public static String convertToLocalMatrixName(String databaseMatrixName){
        StringBuilder localMatrixName = new StringBuilder();
        char symbol;
        for (int i = 0; i < databaseMatrixName.length(); i++) {
            symbol = databaseMatrixName.charAt(i);
            switch (symbol){
                case '1':{
                    localMatrixName.append('₁');
                    break;
                }
                case '2':{
                    localMatrixName.append('₂');
                    break;
                }
                case '3':{
                    localMatrixName.append('₃');
                    break;
                }
                case '4':{
                    localMatrixName.append('₄');
                    break;
                }
                default: {
                    localMatrixName.append(symbol);
                    break;
                }
            }
        }
        return localMatrixName.toString();
    }

    //метод конвертирует химическую формулу из формы для отображения в форму для БД
    public static String convertToDatabaseMatrixName(String localMatrixName){
        StringBuilder databaseMatrixName = new StringBuilder();
        for (int i = 0; i < localMatrixName.length(); i++) {
            switch (localMatrixName.charAt(i)){
                case '₁':{
                    databaseMatrixName.append('1');
                    break;
                }
                case '₂':{
                    databaseMatrixName.append('2');
                    break;
                }
                case '₃':{
                    databaseMatrixName.append('3');
                    break;
                }
                case '₄':{
                    databaseMatrixName.append('4');
                    break;
                }
                default: {
                    databaseMatrixName.append(localMatrixName.charAt(i));
                    break;
                }
            }
        }
        return databaseMatrixName.toString();
    }


}
