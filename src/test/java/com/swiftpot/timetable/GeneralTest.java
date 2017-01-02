package com.swiftpot.timetable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.swiftpot.timetable.util.PrettyJSON;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Ace Programmer Rbk
 *         <Rodney Kwabena Boachie at [rodney@swiftpot.com,rbk.unlimited@gmail.com]> on
 *         02-Jan-17 @ 12:05 AM
 */
public class GeneralTest {

    SchoolObject schoolObject;

    @Before
    public void setupData() {
        List<LecturePeriod> lecturePeriods = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String periodName = "Period " + i;
            int periodNo = i;
            boolean isPeriodAllocated = false;
            lecturePeriods.add(new LecturePeriod(periodName, periodNo, isPeriodAllocated));
        }

        List<Day> days = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String dayName;
            switch (i) {
                case 1:
                    dayName = "MONDAY";
                    break;
                case 2:
                    dayName = "TUESDAY";
                    break;
                case 3:
                    dayName = "WEDNESDAY";
                    break;
                case 4:
                    dayName = "THURSDAY";
                    break;
                case 5:
                    dayName = "FRIDAY";
                    break;
                default:
                    dayName = "NONE";
            }

            int dayNo = i;
            days.add(new Day(dayName, dayNo, lecturePeriods));
        }

        List<YearGroup> yearGroups = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            int yearGroupNumber = i;
            if (Objects.equals(i, 1)) {
                yearGroups.add(new YearGroup(yearGroupNumber, new ArrayList<>(Arrays.asList(new CourseGroup("Science" + 1, days)))));
            } else if (Objects.equals(i, 2)) {
                yearGroups.add(new YearGroup(yearGroupNumber, new ArrayList<>(Arrays.asList(new CourseGroup("Science" + 2, days)))));
            } else if (Objects.equals(i, 3)) {
                yearGroups.add(new YearGroup(yearGroupNumber, new ArrayList<>(Arrays.asList(new CourseGroup("Science" + 3, days)))));
            }

        }

        schoolObject = new SchoolObject();
        schoolObject.setYearGroups(yearGroups);
        System.out.println("School Object Before Changing Periods="+ PrettyJSON.toPrettyFormat(new Gson().toJson(schoolObject)));
    }

    @Test
    public void testObjectChanges() {

        schoolObject.getYearGroups().forEach(yearGroup -> {
            yearGroup.getCourseGroups().forEach(courseGroup -> {
                courseGroup.getDays().forEach(day -> {
                    String dayName = day.getDayName();
                    if (Objects.equals(dayName, "MONDAY")) {
                        day.getLecturePeriods().forEach(lecturePeriod -> {
                            int lecturePeriodNumber = lecturePeriod.getPeriodNumber();
                            if (Objects.equals(lecturePeriodNumber, 1)) {
                                lecturePeriod.setIsPeriodAllocated(true);

                            }
                        });
                    }
                });
            });
        });
        System.out.println("\n\n\n\nSchool Object After Changing Periods=" + toPrettyFormat(new Gson().toJson(schoolObject)));
        final int[] numberOfAllocatedPeriodsExpected = {0};
        schoolObject.getYearGroups().forEach(yearGroup -> {
            yearGroup.getCourseGroups().forEach(courseGroup -> {
                courseGroup.getDays().forEach(day -> {
                    day.getLecturePeriods().forEach(lecturePeriod -> {
                        int lecturePeriodNumber = lecturePeriod.getPeriodNumber();
                        if (Objects.equals(lecturePeriodNumber, 1)) {
                            numberOfAllocatedPeriodsExpected[0]++;
                        }
                    });
                });
            });
        });

        assertThat(numberOfAllocatedPeriodsExpected, equalTo(3));
    }

    /**
     * to see the object in console in a more readable format,with gson dependency
     * @param jsonString
     * @return
     */
    public static String toPrettyFormat(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }

    public class SchoolObject {
        List<YearGroup> yearGroups;

        public List<YearGroup> getYearGroups() {
            return yearGroups;
        }

        public void setYearGroups(List<YearGroup> yearGroups) {
            this.yearGroups = yearGroups;
        }
    }

    public class YearGroup {

        int yearGroupNumber;
        List<CourseGroup> courseGroups;

        public YearGroup(int yearGroupNumber, List<CourseGroup> courseGroups) {
            this.yearGroupNumber = yearGroupNumber;
            this.courseGroups = courseGroups;
        }

        public int getYearGroupNumber() {
            return yearGroupNumber;
        }

        public void setYearGroupNumber(int yearGroupNumber) {
            this.yearGroupNumber = yearGroupNumber;
        }

        public List<CourseGroup> getCourseGroups() {
            return courseGroups;
        }

        public void setCourseGroups(List<CourseGroup> courseGroups) {
            this.courseGroups = courseGroups;
        }
    }

    public class CourseGroup {
        String courseGroupName;
        List<Day> days;

        public CourseGroup(String courseGroupName, List<Day> days) {
            this.courseGroupName = courseGroupName;
            this.days = days;
        }

        public List<Day> getDays() {
            return days;
        }

        public void setDays(List<Day> days) {
            this.days = days;
        }

        public String getCourseGroupName() {
            return courseGroupName;
        }

        public void setCourseGroupName(String courseGroupName) {
            this.courseGroupName = courseGroupName;
        }
    }

    public class Day {
        String dayName;
        int dayNo;
        List<LecturePeriod> lecturePeriods;

        public Day(String dayName, int dayNo, List<LecturePeriod> lecturePeriods) {
            this.dayName = dayName;
            this.lecturePeriods = lecturePeriods;
            this.dayNo = dayNo;
        }

        public String getDayName() {
            return dayName;
        }

        public void setDayName(String dayName) {
            this.dayName = dayName;
        }

        public int getDayNo() {
            return dayNo;
        }

        public void setDayNo(int dayNo) {
            this.dayNo = dayNo;
        }

        public List<LecturePeriod> getLecturePeriods() {
            return lecturePeriods;
        }

        public void setLecturePeriods(List<LecturePeriod> lecturePeriods) {
            this.lecturePeriods = lecturePeriods;
        }
    }

    public class LecturePeriod {
        String periodName;
        int periodNumber;
        boolean isPeriodAllocated;

        public LecturePeriod(String periodName, int periodNumber, boolean isPeriodAllocated) {
            this.periodName = periodName;
            this.periodNumber = periodNumber;
            this.isPeriodAllocated = isPeriodAllocated;
        }

        public String getPeriodName() {
            return periodName;
        }

        public void setPeriodName(String periodName) {
            this.periodName = periodName;
        }

        public int getPeriodNumber() {
            return periodNumber;
        }

        public void setPeriodNumber(int periodNumber) {
            this.periodNumber = periodNumber;
        }

        public boolean isPeriodAllocated() {
            return isPeriodAllocated;
        }

        public void setIsPeriodAllocated(boolean isPeriodAllocated) {
            this.isPeriodAllocated = isPeriodAllocated;
        }
    }
}
