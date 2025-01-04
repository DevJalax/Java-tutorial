import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.npci.upi.exception.NpciErrorException;
import com.npci.upi.util.UPIErrorCodes;

public class SIExecutionValidationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReqPayDebitHelperService.class);
	private static final Set<String> MONTH_WITH_30_DAYS = new HashSet<>();
	private static final Set<String> MONTH_WITH_28_29_DAYS = new HashSet<>();
	private static final Map<Integer, Integer> FORTNIGHTLY = new HashMap<>();
	private static final Map<Integer, String> MONTH_FORTNIGHTLY = new HashMap<>();

	static {
		MONTH_WITH_30_DAYS.add("April");
		MONTH_WITH_30_DAYS.add("June");
		MONTH_WITH_30_DAYS.add("September");
		MONTH_WITH_30_DAYS.add("November");
		MONTH_WITH_28_29_DAYS.add("February");
		FORTNIGHTLY.put(1, 1);
		FORTNIGHTLY.put(2, 2);
		FORTNIGHTLY.put(3, 3);
		FORTNIGHTLY.put(4, 4);
		FORTNIGHTLY.put(5, 5);
		FORTNIGHTLY.put(6, 6);
		FORTNIGHTLY.put(7, 7);
		FORTNIGHTLY.put(8, 8);
		FORTNIGHTLY.put(9, 9);
		FORTNIGHTLY.put(10, 10);
		FORTNIGHTLY.put(11, 11);
		FORTNIGHTLY.put(12, 12);
		FORTNIGHTLY.put(13, 13);
		FORTNIGHTLY.put(14, 14);
		FORTNIGHTLY.put(15, 15);
		FORTNIGHTLY.put(16, 1);
		FORTNIGHTLY.put(17, 2);
		FORTNIGHTLY.put(18, 3);
		FORTNIGHTLY.put(19, 4);
		FORTNIGHTLY.put(20, 5);
		FORTNIGHTLY.put(21, 6);
		FORTNIGHTLY.put(22, 7);
		FORTNIGHTLY.put(23, 8);
		FORTNIGHTLY.put(24, 9);
		FORTNIGHTLY.put(25, 10);
		FORTNIGHTLY.put(26, 11);
		FORTNIGHTLY.put(27, 12);
		FORTNIGHTLY.put(28, 13);
		FORTNIGHTLY.put(29, 14);
		FORTNIGHTLY.put(30, 15);
		FORTNIGHTLY.put(31, 16);

		MONTH_FORTNIGHTLY.put(1, "1_16");
		MONTH_FORTNIGHTLY.put(2, "2_17");
		MONTH_FORTNIGHTLY.put(3, "3_18");
		MONTH_FORTNIGHTLY.put(4, "4_19");
		MONTH_FORTNIGHTLY.put(5, "5_20");
		MONTH_FORTNIGHTLY.put(6, "6_21");
		MONTH_FORTNIGHTLY.put(7, "7_22");
		MONTH_FORTNIGHTLY.put(8, "8_23");
		MONTH_FORTNIGHTLY.put(9, "9_24");
		MONTH_FORTNIGHTLY.put(10, "10_25");
		MONTH_FORTNIGHTLY.put(11, "11_26");
		MONTH_FORTNIGHTLY.put(12, "12_27");
		MONTH_FORTNIGHTLY.put(13, "13_28");
		MONTH_FORTNIGHTLY.put(14, "14_29");
		MONTH_FORTNIGHTLY.put(15, "15_30");
		MONTH_FORTNIGHTLY.put(16, "0_30");

	}

	public static boolean isRuleMatches(Date exeDate, String recurrence, String rule, int value) {

		Calendar exeCalendar = Calendar.getInstance();
		exeCalendar.setTime(exeDate);

		if ("WEEKLY".equals(recurrence)) {
			return isWeeklyRuleMatches(exeCalendar, rule, value);
		} else if ("MONTHLY".equals(recurrence) || "BIMONTHLY".equals(recurrence) || "MONTHLY".equals(recurrence)
				|| "QUARTERLY".equals(recurrence) || "HALFYEARLY".equals(recurrence) || "YEARLY".equals(recurrence)) {
			return isMonthlyRuleMatches(exeCalendar, rule, value);
		} else if ("FORTNIGHTLY".equals(recurrence)) {
			return isFortnightyRuleMatches(exeCalendar, rule, value);
		}

		return true;
	}

	private static boolean isWeeklyRuleMatches(Calendar exeCalendar, String rule, int value) {
		int weekDay = exeCalendar.get(Calendar.DAY_OF_WEEK);
		int exeDateWeekValue = (weekDay + 5) % 7 + 1;
		LOGGER.info("Weekly Mandate Rule {} Rule Value {} current Day Value {}", rule, value, exeDateWeekValue);
		return "ON".equals(rule) && value == exeDateWeekValue || "AFTER".equals(rule) && exeDateWeekValue >= value
				|| "BEFORE".equals(rule) && value >= exeDateWeekValue;
	}

	private static boolean isMonthlyRuleMatches(Calendar exeCalendar, String rule, int value) {
		int monthDay = exeCalendar.get(Calendar.DAY_OF_MONTH);
		LOGGER.info("Month/BI/Quarter/Half/Yearly Mandate Rule {} Rule Value {} current Day Value {}", rule, value,
				monthDay);
		if ("ON".equals(rule)) {
			if (value == monthDay) {
				return true;
			} else if (value == 31) {
				String monthName = getMonthName(exeCalendar);
				return monthDay == 30 && MONTH_WITH_30_DAYS.contains(monthName)
						|| MONTH_WITH_28_29_DAYS.contains(monthName) && isLeapYear(exeCalendar) && monthDay == 29
						|| MONTH_WITH_28_29_DAYS.contains(monthName) && !isLeapYear(exeCalendar) && monthDay == 28;
			} else if (value == 30) {
				String monthName = getMonthName(exeCalendar);
				return MONTH_WITH_28_29_DAYS.contains(monthName) && isLeapYear(exeCalendar) && monthDay == 29
						|| MONTH_WITH_28_29_DAYS.contains(monthName) && !isLeapYear(exeCalendar) && monthDay == 28;
			} else if (value == 29) {
				String monthName = getMonthName(exeCalendar);
				return MONTH_WITH_28_29_DAYS.contains(monthName) && !isLeapYear(exeCalendar) && monthDay == 28;
			}
			return false;
		} else if ("AFTER".equals(rule)) {
			return monthDay >= value || isMonthlyRuleMatches(exeCalendar, "ON", value);
		} else if ("BEFORE".equals(rule)) {
			return value >= monthDay;
		}

		return false;
	}

	private static boolean isFortnightyRuleMatches(Calendar exeCalendar, String rule, int value) {
		int monthDay = exeCalendar.get(Calendar.DAY_OF_MONTH);
		int forthNightDay = FORTNIGHTLY.get(monthDay);
		LOGGER.info("FORTNIGHTLY Mandate Rule {} Rule Value {} current Day Value {}", rule, value, monthDay);
		if ("ON".equals(rule)) {
			if (value == forthNightDay) {
				return true;
			} else if (value == 16) {
				if (monthDay == 15) {
					return true;
				}
				String monthName = getMonthName(exeCalendar);
				return monthDay == 30 && MONTH_WITH_30_DAYS.contains(monthName)
						|| MONTH_WITH_28_29_DAYS.contains(monthName) && isLeapYear(exeCalendar) && monthDay == 29
						|| MONTH_WITH_28_29_DAYS.contains(monthName) && !isLeapYear(exeCalendar) && monthDay == 28;
			} else if (value == 15) {
				String monthName = getMonthName(exeCalendar);
				return MONTH_WITH_28_29_DAYS.contains(monthName) && isLeapYear(exeCalendar) && monthDay == 29
						|| MONTH_WITH_28_29_DAYS.contains(monthName) && !isLeapYear(exeCalendar) && monthDay == 28;
			} else if (value == 14) {
				String monthName = getMonthName(exeCalendar);
				return MONTH_WITH_28_29_DAYS.contains(monthName) && !isLeapYear(exeCalendar) && monthDay == 28;
			}
			return false;
		} else if ("AFTER".equals(rule)) {
			return forthNightDay >= value || isFortnightyRuleMatches(exeCalendar, "ON", value);
		} else if ("BEFORE".equals(rule)) {
			return value >= forthNightDay;
		}

		return false;
	}

	private static Date formatDateStr(String dateStr) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			LOGGER.error("Parse Error for Start Date");
			return null;
		}
	}

	private static String getMonthName(Calendar calendar) {
		SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
		return monthFormat.format(calendar.getTime());
	}

	private static boolean isLeapYear(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	public static int getSequenceNumber(String recurrance, int value, String startDate, Date exeDate)
			throws NpciErrorException {

		Date actualStartDate = getDate(recurrance, value, formatDateStr(startDate));
		Date actualExeDate = getDate(recurrance, value, exeDate);
		if (actualStartDate == null || actualExeDate == null) {
			throw new NpciErrorException(UPIErrorCodes.XB.getCode(), UPIErrorCodes.XB.getMessage());
		}
		if ("WEEKLY".equals(recurrance)) {
			return calculateCalendarWeeks(actualStartDate, actualExeDate);
		} else if ("MONTHLY".equals(recurrance)) {
			return calculateMonthsBetween(actualStartDate, actualExeDate);
		} else if ("BIMONTHLY".equals(recurrance)) {
			return calculateBiMonthsBetween(actualStartDate, actualExeDate);
		} else if ("QUARTELY".equals(recurrance)) {
			return calculateQuarBetween(actualStartDate, actualExeDate);
		} else if ("HALFYEARLY".equals(recurrance)) {
			return calculateHalfYearlyBetween(actualStartDate, actualExeDate);
		} else if ("YEARLY".equals(recurrance)) {
			return calculateYearBetween(actualStartDate, actualExeDate);
		} else if ("FORTNIGHTLY".equals(recurrance)) {
			return calculateForthNightBetween(actualStartDate, actualExeDate);
		} else {
			throw new NpciErrorException(UPIErrorCodes.XB.getCode(), UPIErrorCodes.XB.getMessage());
		}

	}

	public static Date getDate(String recurrance, int value, Date dte) {
		Calendar exeCalendar = Calendar.getInstance();
		exeCalendar.setTime(dte);

		if ("WEEKLY".equals(recurrance)) {

			int weekDay = exeCalendar.get(Calendar.DAY_OF_WEEK);
			int dateWeekValue = (weekDay + 5) % 7 + 1;
			System.out.println(dateWeekValue);
			if (dateWeekValue == value) {
				return dte;
			}

			else if (dateWeekValue > value) {
				int diff = dateWeekValue - value;

				exeCalendar.add(Calendar.DAY_OF_MONTH, -diff);
				Date date = exeCalendar.getTime();
				return date;
			} else {

				int diff = value - dateWeekValue;
				exeCalendar.add(Calendar.DAY_OF_MONTH, diff);
				Date date = exeCalendar.getTime();
				return date;

			}

		}

		else if ("MONTHLY".equals(recurrance) || "BIMONTHLY".equals(recurrance) || "QUARTELY".equals(recurrance)
				|| "HALFYEARLY".equals(recurrance) || "YEARLY".equals(recurrance)) {
			int monthDayValue = exeCalendar.get(Calendar.DAY_OF_MONTH);
			if (monthDayValue == value) {
				return dte;
			} else if (monthDayValue < value) {
				String monthName = getMonthName(exeCalendar);
				if (value == 31 && monthDayValue == 30 && MONTH_WITH_30_DAYS.contains(monthName)) {

					return dte;

				} else if ((value == 30 || value == 31) && MONTH_WITH_28_29_DAYS.contains(monthName)
						&& (monthDayValue == 28 || monthDayValue == 29)) {

					return dte;
				} else {
					exeCalendar.set(Calendar.DAY_OF_MONTH, value);
					Date date = exeCalendar.getTime();
					return date;
				}
			} else {
				exeCalendar.set(Calendar.DAY_OF_MONTH, value);
				Date date = exeCalendar.getTime();
				return date;
			}
		} else if ("FORTNIGHTLY".equals(recurrance)) {
			int monthDayValue = exeCalendar.get(Calendar.DAY_OF_MONTH);
			int fortnightValue = FORTNIGHTLY.get(monthDayValue);
			if (fortnightValue == value) {
				return dte;
			} else if (fortnightValue < value) {
				String monthName = getMonthName(exeCalendar);
				if (value == 16 && monthDayValue == 30 && MONTH_WITH_30_DAYS.contains(monthName)) {
					return dte;
				}

				else if ((value == 15 || value == 16) && MONTH_WITH_28_29_DAYS.contains(monthName)
						&& (monthDayValue == 28 || monthDayValue == 29)) {

					return dte;
				} else {
					if (monthDayValue <= 15) {
						if (value == 16 && monthDayValue == 15) {
							exeCalendar.set(Calendar.DAY_OF_MONTH, monthDayValue);
						} else {
							exeCalendar.set(Calendar.DAY_OF_MONTH, value);
						}
						Date date = exeCalendar.getTime();
						return date;
					} else {
						String monthDayStr = MONTH_FORTNIGHTLY.get(value).split("_")[1];
						exeCalendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(monthDayStr));
						Date date = exeCalendar.getTime();
						return date;
					}
				}
			} else {
				if (monthDayValue <= 15) {
					if (value == 16 && monthDayValue == 15) {
						exeCalendar.set(Calendar.DAY_OF_MONTH, monthDayValue);
					} else {
						exeCalendar.set(Calendar.DAY_OF_MONTH, value);
					}
					Date date = exeCalendar.getTime();
					return date;
				} else {
					String monthDayStr = MONTH_FORTNIGHTLY.get(value).split("_")[1];
					System.out.println("monthDayStr " + monthDayStr);
					exeCalendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(monthDayStr));
					Date date = exeCalendar.getTime();
					return date;
				}

			}
		}

		return null;
	}

	private static int calculateCalendarWeeks(Date startDate, Date currentExeDate) {
		int weeks = 0;
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(currentExeDate);

		int dayOfWeek = startCalendar.get(Calendar.DAY_OF_WEEK);

		while (startCalendar.before(endCalendar) || startCalendar.equals(endCalendar)) {
			// Check if the current day is the start of a new week (Sunday)
			if (startCalendar.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
				weeks++;
			}
			// Move to the next day
			startCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		return weeks;
	}

	private static int calculateMonthsBetween(Date startDate, Date endDate) {
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(startDate);
		startDateCalendar.set(Calendar.DAY_OF_MONTH, 1);
		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(endDate);
		endDateCalendar.set(Calendar.DAY_OF_MONTH, 1);

		int monthsBetween = 1;

		while (startDateCalendar.before(endDateCalendar)) {
			monthsBetween++;
			startDateCalendar.add(Calendar.MONTH, 1);
		}

		return monthsBetween;
	}

	private static int calculateBiMonthsBetween(Date startDate, Date endDate) {
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(startDate);
		startDateCalendar.set(Calendar.DAY_OF_MONTH, 1);

		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(endDate);
		endDateCalendar.set(Calendar.DAY_OF_MONTH, 1);
		int biMonthIntervals = 0;

		// Loop through each bi-monthly interval and check if it's within the range
		Calendar currentInterval = (Calendar) startDateCalendar.clone();
		while (currentInterval.before(endDateCalendar) || currentInterval.equals(endDateCalendar)) {
			biMonthIntervals++;

			// Move to the next bi-monthly interval
			currentInterval.add(Calendar.MONTH, 2);
		}

		return biMonthIntervals;
	}

	private static int calculateQuarBetween(Date startDate, Date endDate) {
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(startDate);
		startDateCalendar.set(Calendar.DAY_OF_MONTH, 1);

		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(endDate);
		endDateCalendar.set(Calendar.DAY_OF_MONTH, 1);

		int quarterlyIntervals = 0;

		// Loop through each quarterly interval and check if it's within the range
		Calendar currentInterval = (Calendar) startDateCalendar.clone();
		while (currentInterval.before(endDateCalendar) || currentInterval.equals(endDateCalendar)) {
			quarterlyIntervals++;

			// Move to the next quarterly interval
			currentInterval.add(Calendar.MONTH, 3);
		}

		return quarterlyIntervals;

	}

	private static int calculateHalfYearlyBetween(Date startDate, Date endDate) {
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(startDate);
		startDateCalendar.set(Calendar.DAY_OF_MONTH, 1);

		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(endDate);
		endDateCalendar.set(Calendar.DAY_OF_MONTH, 1);

		int halfYearlyIntervals = 0;

		// Loop through each half-yearly interval and check if it's within the range
		Calendar currentInterval = (Calendar) startDateCalendar.clone();
		while (currentInterval.before(endDateCalendar) || currentInterval.equals(endDateCalendar)) {
			halfYearlyIntervals++;

			// Move to the next half-yearly interval
			currentInterval.add(Calendar.MONTH, 6);
		}

		return halfYearlyIntervals;

	}

	private static int calculateYearBetween(Date startDate, Date endDate) {
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(startDate);
		startDateCalendar.set(Calendar.DAY_OF_MONTH, 1);

		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(endDate);
		endDateCalendar.set(Calendar.DAY_OF_MONTH, 1);

		int yearlyIntervals = 0;

		// Loop through each yearly interval and check if it's within the range
		Calendar currentInterval = (Calendar) startDateCalendar.clone();
		while (currentInterval.before(endDateCalendar) || currentInterval.equals(endDateCalendar)) {
			yearlyIntervals++;

			// Move to the next yearly interval
			currentInterval.add(Calendar.YEAR, 1);
		}

		return yearlyIntervals;
	}

	private static int calculateForthNightBetween(Date startDate, Date endDate) {
		int intial = 0;
		int end = 0;
		Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(startDate);
		int startDayValue = startDateCalendar.get(Calendar.DAY_OF_MONTH);
		if (startDayValue >= 16) {
			startDateCalendar.add(Calendar.MONTH, 1);
			intial = 1;
		}

		Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(endDate);
		int endDateValue = endDateCalendar.get(Calendar.DAY_OF_MONTH);
		if (endDateValue <= 15) {
			startDateCalendar.add(Calendar.MONTH, 1);
			end = 1;
		}

		int monthsBetween = 1;
		startDateCalendar.set(Calendar.DAY_OF_MONTH, 1);
		endDateCalendar.set(Calendar.DAY_OF_MONTH, 1);
		while (startDateCalendar.before(endDateCalendar)) {
			monthsBetween++;
			startDateCalendar.add(Calendar.MONTH, 1);
		}

		return monthsBetween * 2 + intial + end;
	}

	public static boolean validateMonthForBiQuarterHalfYearly(String recurrence, String startDate, Date date) {
		Date stdate = formatDateStr(startDate);
		Calendar stDate = Calendar.getInstance();
		stDate.setTime(stdate);
		int startMonth = stDate.get(Calendar.MONTH) + 1;
		System.out.println(startMonth);
		Calendar exeDate = Calendar.getInstance();
		exeDate.setTime(date);
		int exeMonth = exeDate.get(Calendar.MONTH) + 1;
		System.out.println(exeMonth);
		LOGGER.info("validateMonthForBiQuarterHalfYearly Recuurence {} startMonth Value {} exe Month Value {}",
				recurrence, startMonth, exeMonth);
		if ("BIMONTHLY".equals(recurrence)) {
			return (biMonthlyFirstPattern(startMonth) && biMonthlyFirstPattern(exeMonth))
					|| (biMonthlySecondPattern(startMonth) && biMonthlySecondPattern(exeMonth));
		} else if ("QUARTELY".equals(recurrence)) {
			return (quaterlyFirstPattern(startMonth) && quaterlyFirstPattern(exeMonth))
					|| (quaterlySecondPattern(startMonth) && quaterlySecondPattern(exeMonth))
					|| (quaterlyThirdPattern(startMonth) && quaterlyThirdPattern(exeMonth));
		} else if ("HALFYEARLY".equals(recurrence)) {
			return halfYearlyFirstPattern(startMonth, exeMonth) || halfYearlySecondPattern(startMonth, exeMonth)
					|| halfYearlyThirdPattern(startMonth, exeMonth) || halfYearlyFourthPattern(startMonth, exeMonth)
					|| halfYearlyFifthPattern(startMonth, exeMonth) || halfYearlySixthPattern(startMonth, exeMonth);
		} else if ("YEARLY".equals(recurrence)) {
			return (startMonth == exeMonth);
		}
		
		return true;

	}

	// 1 3 5 7 9 11
	private static boolean biMonthlyFirstPattern(int num) {
		// Check if the number matches the first pattern
		return num % 2 != 0 && num >= 1 && num <= 11;
	}

	// 2 4 6 8 10 12
	private static boolean biMonthlySecondPattern(int num) {
		// Check if the number matches the second pattern
		return num % 2 == 0 && num >= 2 && num <= 12;
	}

	// 1 4 7 10
	private static boolean quaterlyFirstPattern(int num) {
		// Check if the number matches the first pattern
		return num % 3 == 1 && num >= 1 && num <= 10;
	}

	// 2 5 8 11
	private static boolean quaterlySecondPattern(int num) {
		// Check if the number matches the second pattern
		return num % 3 == 2 && num >= 2 && num <= 11;
	}

	// 3 6 9 12
	private static boolean quaterlyThirdPattern(int num) {
		// Check if the number matches the third pattern
		return num % 3 == 0 && num >= 3 && num <= 12;
	}

	// 1 7
	private static boolean halfYearlyFirstPattern(int num1, int num2) {
		return (num1 == 1 && num2 == 7) || (num1 == 7 && num2 == 1);
	}

	// 2 8
	private static boolean halfYearlySecondPattern(int num1, int num2) {
		return (num1 == 2 && num2 == 8) || (num1 == 8 && num2 == 2);
	}

	// 3 9
	private static boolean halfYearlyThirdPattern(int num1, int num2) {
		return (num1 == 3 && num2 == 9) || (num1 == 9 && num2 == 3);
	}

	// 4 10
	private static boolean halfYearlyFourthPattern(int num1, int num2) {
		return (num1 == 4 && num2 == 10) || (num1 == 10 && num2 == 4);
	}

	// 5 11
	private static boolean halfYearlyFifthPattern(int num1, int num2) {
		return (num1 == 5 && num2 == 11) || (num1 == 11 && num2 == 5);
	}

	// 6 12
	private static boolean halfYearlySixthPattern(int num1, int num2) {
		return (num1 == 6 && num2 == 12) || (num1 == 12 && num2 == 6);
	}

}
