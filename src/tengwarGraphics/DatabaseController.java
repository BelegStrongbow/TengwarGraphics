package tengwarGraphics;

import javafx.scene.paint.Color;

import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class DatabaseController {

    private static final String CONN = "jdbc:sqlite:database.sqlite";

    public static ArrayList<TengwarImage> getImagesFromDatabase() throws SQLException {

        ArrayList<TengwarImage> tengwarImages = new ArrayList<>();

        String sqlQuery = "SELECT * FROM savedImages;";
        Connection connection = getConnection(CONN);
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        try {
            try {
                try {
                    try {
                        while (resultSet.next()) {
                            String name = resultSet.getString("name");
                            String date = resultSet.getString("date");
                            String backgroundColor = resultSet.getString("backgroundColor");
                            String backroundImageLoc = resultSet.getString("backgroundImageLoc");
                            int typeOfBackground = resultSet.getInt("typeOfBackground");
                            String filters = resultSet.getString("filters");
                            Boolean textOnTop = resultSet.getBoolean("textOnTopOfFilters");
                            String textOriginal = resultSet.getString("textOriginal");
                            int size = resultSet.getInt("size");
                            String textColor = resultSet.getString("textColor");
                            String fontEnum = resultSet.getString("fontEnum");

                            String[] col = backgroundColor.split(";");
                            Color backgroundCol = new Color(Float.parseFloat(col[0]), Float.parseFloat(col[1]), Float.parseFloat(col[2]), 1);
                            col = textColor.split(";");
                            Color textCol = new Color(Float.parseFloat(col[0]), Float.parseFloat(col[1]), Float.parseFloat(col[2]), 1);

                            String[] filterArray = filters.split(";");
                            ArrayList<FilterEnum> filterArrayList = new ArrayList<>();
                            for (int i = 0; i < filterArray.length; i++) {
                                filterArrayList.add(FilterEnum.valueOf(filterArray[i]));
                            }

                            String[] namedate = new String[2];
                            namedate[0] = name;
                            namedate[1] = date;

                            tengwarImages.add(new TengwarImage(backgroundCol, new TengwarText(textOriginal, size, 710, 335, textCol, TengwarFont.valueOf(fontEnum)), new ArrayList<Action>(), backroundImageLoc, typeOfBackground, filterArrayList, textOnTop));
                            tengwarImages.get(tengwarImages.size()-1).setName(name);
                            tengwarImages.get(tengwarImages.size()-1).setDate(date);


                        }
                    } finally {
                        resultSet.close();
                    }
                } finally {
                    preparedStatement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tengwarImages;
    }

    public static void saveImageInDatabase(TengwarImage tengwarImage, String name) throws SQLException {
        Connection connection = getConnection(CONN);

        String insertSQL = "INSERT INTO savedImages(name, date, backgroundColor, backgroundImageLoc, typeOfBackground, filters, textOnTopOfFilters, textOriginal, size, textColor, fontEnum) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

        LocalDateTime now = LocalDateTime.now();
        String date = now.getYear()+"-"+now.getMonthValue()+"-"+now.getDayOfMonth()+" "+now.getHour()+":"+now.getMinute()+":"+now.getSecond();
        String backgroundColor = tengwarImage.background.getRed() + ";" + tengwarImage.background.getGreen() + ";" + tengwarImage.background.getBlue();
        String filters="";
        for (int i = 0; i < tengwarImage.filters.size(); i++) {
            filters+=tengwarImage.filters.get(i).toString()+";";
        }
        Color col = tengwarImage.tengwarText.getColor();
        String textColor = col.getRed() + ";" + col.getGreen() + ";" + col.getBlue();

        try {
            try {
                try {
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, date);
                    preparedStatement.setString(3, backgroundColor);
                    preparedStatement.setString(4, tengwarImage.backImageLocation);
                    preparedStatement.setInt(5, tengwarImage.typeOfBackground);
                    preparedStatement.setString(6, filters);
                    preparedStatement.setBoolean(7, tengwarImage.textOnTopOfFilters);
                    preparedStatement.setString(8, tengwarImage.tengwarText.getTextOriginal());
                    preparedStatement.setInt(9, tengwarImage.tengwarText.getSize());
                    preparedStatement.setString(10, textColor);
                    preparedStatement.setString(11, tengwarImage.tengwarText.getFontEnum().toString());
                    preparedStatement.executeUpdate();
                } finally {
                    preparedStatement.close();
                }
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
