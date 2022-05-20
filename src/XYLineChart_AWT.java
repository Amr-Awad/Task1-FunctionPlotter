import java.awt.Color;
import java.awt.BasicStroke;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import javax.swing.*;

public class XYLineChart_AWT extends ApplicationFrame {

    private static String equation;
    private static int minValue;
    private static int maxValue;
    private static ArrayList<Integer> scalePoints;
    private static boolean dividedByZero = false;

    private char []chars = new char[]{'0','1','2','3','4','5','6','7','8','9','x','X','*','/','-','+','^'};
    private static final ArrayList<Character> equationCharacters = new ArrayList<Character>();
    private static final ArrayList<Character> equationNumbers = new ArrayList<Character>();
    private static final ArrayList<Character> equationVariables = new ArrayList<Character>();

    private static ArrayList<TypesCombination> translatedequation = new ArrayList<TypesCombination>();

    private static class XYPoint
    {
        private double x;
        private double y;

        XYPoint(double x , double y)
        {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

    }

    public static void calculateScalePoints()
    {
        scalePoints = new ArrayList<>();
        int scale;
        if(maxValue-minValue<=19)
            scale = 1;
        else
            scale = (maxValue - minValue) / 10;
        for(int x = minValue ; x<=maxValue ; x+=scale)
        {
            scalePoints.add(x);
        }
    }

    public static void addAllEquationCharacters()
    {
        equationCharacters.add('*');
        equationCharacters.add('/');
        equationCharacters.add('-');
        equationCharacters.add('+');
        equationCharacters.add('^');
        equationCharacters.add('x');
        equationCharacters.add('X');

        equationNumbers.add('0');
        equationNumbers.add('1');
        equationNumbers.add('2');
        equationNumbers.add('3');
        equationNumbers.add('4');
        equationNumbers.add('5');
        equationNumbers.add('6');
        equationNumbers.add('7');
        equationNumbers.add('8');
        equationNumbers.add('9');

        equationVariables.add('x');
        equationVariables.add('X');
    }
    public static boolean validateEquation()
    {
        String equation1=equation;
        if(!equationNumbers.contains(equation1.charAt(equation1.length()-1))&&!equationVariables.contains(equation1.charAt(equation1.length()-1)))
        {
            return false;
        }
        if(equation1.charAt(0)=='-')
        {
            equation1 = equation1.substring(1);
        }
        for(int i =0 ; i<equation1.length();i++)
        {
            if(i%2==0)
            {
                if(!equationNumbers.contains(equation1.charAt(i))&&!equationVariables.contains(equation1.charAt(i)))
                {
                    return false;
                }
                if(i+3<equation1.length())
                {
                    if(equationVariables.contains(equation1.charAt(i))||equation1.charAt(i)=='0')
                        if(equation1.charAt(i+1)=='^')
                            if(equation1.charAt(i+2)=='-')
                                if(equation1.charAt(i+3)=='1')
                                    if(equation1.charAt(i)=='0')
                                        dividedByZero = true;
                                    for (int j=0;j<scalePoints.size();j++)
                                        if(scalePoints.get(j)==0)
                                            dividedByZero = true;
                }
            }
            if(i%2==1)
            {
                if(!equationCharacters.contains(equation1.charAt(i)))
                {
                    return false;
                }
                if(equation.charAt(i)=='/')
                    if(equation.charAt(i+1)=='0')
                        dividedByZero = true;

                if(equation1.charAt(i+1)=='-')
                {
                    equation1 = equation1.substring(0,i)+equation1.substring(i+1);
                }
            }
        }
        return true;
    }

    public static ArrayList<TypesCombination> translateEquation()
    {
        String equation1 = equation;
        boolean isNegative = false;
        boolean charIsNegative = false;
        if(equation1.charAt(0) == '-')
        {
            if(!equationVariables.contains(equation1.charAt(1)))
                isNegative=true;
            else
                charIsNegative = true;
            equation1 = equation1.substring(1);
        }
        for(int i = 0 ; i<equation1.length() ; i++)
        {
            double num = 0;
            boolean isInt = false;
            for (int j = i ; j<equation1.length() ; j++)
            {
                if(equationCharacters.contains(equation1.charAt(j))||equationVariables.contains(equation1.charAt(j)))
                    break;

                int c = equation1.charAt(j);
                num = (num*10) + (c - 48);
                isInt = true;
                i = j;
            }
            if(isInt)
            {
                if(isNegative)
                {
                    num *= -1;
                    isNegative = false;
                }
                TypesCombination type1 = new IntType(num);
                translatedequation.add(type1);
                continue;
            }
            if(i+1<equation1.length())
            {
                if(equation1.charAt(i+1)=='-'&&(!equationVariables.contains(i)&&!equationVariables.contains(i+2)))
                {
                    equation1 = equation1.substring(0,i+1) + equation1.substring(i+2);
                    isNegative =true;
                }
                else if(equation1.charAt(i+1)=='-'&&!equationVariables.contains(i))
                {
                    TypesCombination type2 = new CharType(equation1.charAt(i),false);
                    translatedequation.add(type2);
                    charIsNegative = true;
                    equation1 = equation1.substring(0,i+1) + equation1.substring(i+2);
                    continue;
                }
                else if(equationVariables.contains(i)&&charIsNegative)
                {
                    TypesCombination type2 = new CharType(equation1.charAt(i),true);
                    translatedequation.add(type2);
                    charIsNegative = false;
                    continue;
                }
            }
                TypesCombination type2 = new CharType(equation1.charAt(i),false);
                translatedequation.add(type2);
        }
        return translatedequation;
    }

    public static ArrayList<TypesCombination> changeElementsFromEquation(int position,double X,ArrayList<TypesCombination> translatedequation)
    {
        if (translatedequation.get(position).getValue() == 120 || translatedequation.get(position).getValue() == 88)
        {
            if (translatedequation.get(position+2).getValue() == 120 || translatedequation.get(position+2).getValue() == 88)
            {
                double newX1 = X;
                if(translatedequation.get(position).isNegative)
                    newX1 *= -1;

                double newX2 = X;
                if(translatedequation.get(position+2).isNegative)
                    newX2 *= -1;

                IntType temp;
                if(translatedequation.get(position+1).getValue() == 42 ||translatedequation.get(position+1).getValue() == 47) {
                    if (translatedequation.get(position - 1).getValue() == 45) {
                        newX1 *= -1;

                        CharType temp2 = new CharType('+', false);
                        translatedequation.remove(position - 1);
                        translatedequation.add(position - 1, temp2);
                    }
                }

                if (translatedequation.get(position+1).getValue() == 47)
                    temp = new IntType(newX1 / newX2);
                else if (translatedequation.get(position+1).getValue() == 94)
                    temp = new IntType(Math.pow(newX1, newX2));
                else
                    temp = new IntType(newX1 * newX2);


                translatedequation.remove(position);
                translatedequation.remove(position);
                translatedequation.remove(position);

                translatedequation.add(position, temp);
            }
            else
            {
                double newX = X;
                if(translatedequation.get(position).isNegative)
                    newX *= -1;

                if(translatedequation.get(position+1).getValue() == 42 ||translatedequation.get(position+1).getValue() == 47) {
                    if (translatedequation.get(position - 1).getValue() == 45) {
                        newX *= -1;

                        CharType temp2 = new CharType('+', false);
                        translatedequation.remove(position - 1);
                        translatedequation.add(position - 1, temp2);
                    }
                }

                IntType temp;
                if (translatedequation.get(position + 1).getValue() == 47)
                    temp = new IntType(newX / translatedequation.get(position + 2).getValue());
                else if (translatedequation.get(position + 1).getValue() == 94)
                    temp = new IntType(Math.pow(newX, translatedequation.get(position + 2).getValue()));
                else
                    temp = new IntType(newX * translatedequation.get(position + 2).getValue());

                translatedequation.remove(position);
                translatedequation.remove(position);
                translatedequation.remove(position);

                translatedequation.add(position, temp);
            }
        }
        else if (translatedequation.get(position+2).getValue() == 120 || translatedequation.get(position+2).getValue() == 88)
        {
            double newX = X;
            if(translatedequation.get(position+2).isNegative)
                newX *= -1;

            double newValue = translatedequation.get(position).getValue();
            if(translatedequation.get(position+1).getValue() == 42 ||translatedequation.get(position+1).getValue() == 47) {
                if (translatedequation.get(position - 1).getValue() == 45) {
                    newValue *= -1;

                    CharType temp2 = new CharType('+', false);
                    translatedequation.remove(position - 1);
                    translatedequation.add(position - 1, temp2);
                }
            }

            IntType temp;

            if (translatedequation.get(position+1).getValue() == 47)
                temp = new IntType(newValue / newX);
            else if (translatedequation.get(position+1).getValue() == 94)
                temp = new IntType(Math.pow(newValue, newX));
            else
                temp = new IntType(newValue * newX);

            translatedequation.remove(position);
            translatedequation.remove(position);
            translatedequation.remove(position);

            translatedequation.add(position, temp);

        }
        else
        {
            double newValue = translatedequation.get(position).getValue();
            if(translatedequation.get(position+1).getValue() == 42 ||translatedequation.get(position+1).getValue() == 47) {
                if (translatedequation.get(position - 1).getValue() == 45) {
                    newValue *= -1;

                    CharType temp2 = new CharType('+', false);
                    translatedequation.remove(position - 1);
                    translatedequation.add(position - 1, temp2);
                }
            }

            IntType temp;
            if (translatedequation.get(position + 1).getValue() == 47)
                temp = new IntType(newValue / translatedequation.get(position+2).getValue());
            else if (translatedequation.get(position+1).getValue() == 94)
                temp = new IntType(Math.pow(newValue, translatedequation.get(position+2).getValue()));
            else
                temp = new IntType(newValue * translatedequation.get(position+2).getValue());

            translatedequation.remove(position);
            translatedequation.remove(position);
            translatedequation.remove(position);

            translatedequation.add(position, temp);
        }
        return translatedequation;
    }

    public static XYPoint calculatePoints(double X,ArrayList<TypesCombination> translatedequation)
    {
        double sum = 0;
        if (translatedequation.get(0).isCharacter())
        {
            if (translatedequation.get(0).isNegative())
                sum-=X;
            else
                sum += X;
        }
        else
        {
            sum += translatedequation.get(0).getValue();
        }

        for (int i = 1; i < translatedequation.size(); i = i + 2)
        {
            if (i + 2 < translatedequation.size()) {
                if ((translatedequation.get(i + 2).getValue() == 47 && translatedequation.get(i).getValue() != 94) || (translatedequation.get(i + 2).getValue() == 42 && translatedequation.get(i).getValue() != 94) || translatedequation.get(i + 2).getValue() == 94) {
                    if (i + 4 < translatedequation.size()) {
                        if (translatedequation.get(i + 4).getValue() == 94) {
                            translatedequation = changeElementsFromEquation(i + 3, X,translatedequation);
                            i=i-2;
                            continue;
                        }

                    }
                    translatedequation = changeElementsFromEquation(i + 1, X,translatedequation);
                    i=i-2;
                    continue;
                }
            }
                if (translatedequation.get(i + 1).getValue() == 120 || translatedequation.get(i + 1).getValue() == 88)
                {
                    double newX = X;
                    if (translatedequation.get(i + 1).isNegative)
                        newX *= -1;

                    if (translatedequation.get(i).getValue() == 43)
                        sum += newX;
                    else if (translatedequation.get(i).getValue() == 45)
                        sum -= newX;
                    else if (translatedequation.get(i).getValue() == 42)
                        sum *= newX;
                    else if (translatedequation.get(i).getValue() == 47)
                        sum /= newX;
                    else
                        sum = Math.pow(sum, newX);
                }
                else
                {
                    if (translatedequation.get(i).getValue() == 43)
                        sum += translatedequation.get(i + 1).getValue();
                    else if (translatedequation.get(i).getValue() == 45)
                        sum -= translatedequation.get(i + 1).getValue();
                    else if (translatedequation.get(i).getValue() == 42)
                        sum *= translatedequation.get(i + 1).getValue();
                    else if (translatedequation.get(i).getValue() == 47)
                        sum /= translatedequation.get(i + 1).getValue();
                    else
                        sum = Math.pow(sum, translatedequation.get(i + 1).getValue());
                }
            }
        XYPoint point = new XYPoint(X, sum);
        return point;
    }

    public static ArrayList<XYPoint> getAllPoints()
    {
        int scale;
        ArrayList<XYPoint> Points = new ArrayList<>();

        for(int i = 0 ; i<scalePoints.size() ; i++)
        {
            ArrayList<TypesCombination> temp= new ArrayList<>();
            for(int j = 0 ; j<translatedequation.size() ; j++)
            {
                temp.add(translatedequation.get(j));
            }
            Points.add(calculatePoints(scalePoints.get(i),temp));
        }
        System.out.println("Equation    y="+equation);
        System.out.println("    X   Y");
        for(int i = 0 ; i<Points.size() ; i++)
        {
            System.out.println(Points.get(i).getX()+"   "+Points.get(i).getY() );
        }
        System.out.println("\n\n");
        return Points;
    }

    public XYLineChart_AWT( String applicationTitle, String chartTitle ) {
        super(applicationTitle);
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle ,
                "X" ,
                "Y" ,
                createDataset() ,
                PlotOrientation.VERTICAL ,
                true , true , false);

        ChartPanel chartPanel = new ChartPanel( xylineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 1000 , 1000 ) );
        final XYPlot plot = xylineChart.getXYPlot( );

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true,true);
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesPaint( 1 , Color.GREEN );
        renderer.setSeriesPaint( 2 , Color.YELLOW );
        renderer.setSeriesStroke( 0 , new BasicStroke( 1.0f ) );
        renderer.setSeriesStroke( 1 , new BasicStroke( 1.0f ) );
        renderer.setSeriesStroke( 2 , new BasicStroke( 1.0f ) );
        plot.setRenderer( renderer );
        setContentPane( chartPanel );
    }

    private XYDataset createDataset( )
    {

        final XYSeries EquationPoints = new XYSeries( equation );
        ArrayList<XYPoint> Points = getAllPoints();
        for (int i =0 ; i<Points.size() ; i++)
            EquationPoints.add( Points.get(i).getX() , Points.get(i).getY() );


        final XYSeriesCollection dataset = new XYSeriesCollection( );
        dataset.addSeries(EquationPoints);
        return dataset;
    }

    public static void main( String[ ] args ) throws InterruptedException {
        addAllEquationCharacters();
        while (true)
        {
            translatedequation = new ArrayList<>();
            equation = JOptionPane.showInputDialog("Enter Your Equation:    ");

            minValue = Integer.parseInt(JOptionPane.showInputDialog("Enter Your Minimum Value :    "));
            maxValue = Integer.parseInt(JOptionPane.showInputDialog("Enter Your Maximum Value :    "));
            if(maxValue-minValue<0)
            {

                JOptionPane.showMessageDialog(null,"Maximum number must be greater than or equal to Minimum number!!!");
                continue;
            }
            calculateScalePoints();
            if(!validateEquation())
            {

                JOptionPane.showMessageDialog(null,"Please Enter a valid equation!!!");
                continue;
            }
            if(dividedByZero)
            {
                JOptionPane.showMessageDialog(null,"A Number is divided by zero, please try again!!!");
                continue;
            }

            translateEquation();

            XYLineChart_AWT chart = new XYLineChart_AWT("Arbitrary Plot Chat",
                    "Equation Graph");
            chart.pack( );
            RefineryUtilities.centerFrameOnScreen( chart );
            chart.setVisible( true );

            TimeUnit.SECONDS.sleep(5);
            if(JOptionPane.showConfirmDialog(null,"Do You want to make a chart for another equation?")==1)
            {
                break;
            }
        }
    }
}