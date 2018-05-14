package logic;

public class Converter {

    public static String convertToViewName(String viewNameRussian){
        switch (viewNameRussian){
            case "Свойства керамических нанокомпозитов":{
                return "properties_view";
            }
            case "Статьи по керамическим нанокомпозитам":{
                return "articles";
            }
            case "Свойства кермических нанокомпозитов (старые)":{
                return "properties_view_old";
            }
            case "Область применения керамических нанокомпозитов":{
                return "application_area";
            }
            default: throw new RuntimeException();
        }
    }

//    public static String convertToLocalFillName(String databaseFillName){
//        return convertToLocalMatrixName(databaseFillName);
//    }

    public static String convertToLocalMatrixName(String databaseMatrixName){
        StringBuilder localMatrixName = new StringBuilder();
        for (int i = 0; i < databaseMatrixName.length(); i++) {
            switch (databaseMatrixName.charAt(i)){
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
                    localMatrixName.append(databaseMatrixName.charAt(i));
                    break;
                }
            }
        }
        return localMatrixName.toString();
    }

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
