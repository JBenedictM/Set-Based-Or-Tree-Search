package ParseData;

import Parser.TimeConverter;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public class Slot {

    public Day day;          //The day of the week the slot starts on
    public float time;       //Time represented as a decimal 24hr. i.e 1:30pm = 13.5
    public int max;          //The max that this slot can hold for courses (remember slots are separated into two differed lists, one for courses, and one for labs)
    public int min;          //The min that this slot should hold. (Soft Constraint)


    public static class DayConvertError extends Exception{

    }

    public enum Day{
        Mon("MO"),
        Tues("TU"),
        Fri("FR");

        private String dayString;
        Day(String dayString){
            this.dayString = dayString;
        }

        public String getDayString(){
            return this.dayString;
        }
    };

    public Slot(Day day, float time, int max, int min){
        this.day = day;
        this.time = time;
        this.max = max;
        this.min = min;
    }


    //Justification: If two slots are on the same day and at the same time they are "the same slot"
    //e.g the course slot times can't have two slots on tues for 1pm.
    //This way I can detect if there is an invalid redeclaration of a course/lab slot.

     @Override
     public boolean equals(Object o) {

         if (o == this) return true;
         if (!(o instanceof Slot)) {
             return false;
         }
         Slot otherSlot = (Slot) o;
         return this.day.equals(otherSlot.day) &&
                 this.time == otherSlot.time;
     }

     @Override
     public int hashCode() {
         return Objects.hash(day, time, max, min);
     }

     public static Day toDay(String str) throws DayConvertError{

         if (str.equals("MO")) {						//Converts String to enum Day
             return Day.Mon;
         } else if (str.equals("TU")) {
             return Day.Tues;
         } else if (str.equals("FR")) {
             return Day.Fri;
         } else {
             throw new DayConvertError();
         }
     }



    @Override
    public String toString(){
        TimeConverter converter = new TimeConverter();

        Optional<Map.Entry<String, Float>> stringTime = converter.timeConvert.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), this.time))
                .findAny();

        return this.day.getDayString() + ", " + stringTime.get().getKey();
    }
}
