package com.app.oktpus.constants.carfinance;


/**
 * Created by Gyandeep on 16/1/18.
 */

public class FinanceConstants {

    public static final class LoanOrFinanceType{
        public static final String
            NEW_OR_USED_AUTO    = "New or Used Auto (Dealer)",
            REFINANCE_AUTO      = "Refinance Auto",
            PRIVATE_PARTY       = "Private Party (Non Dealer)",
            AUTO_LEASE_BUYOUT   = "Auto Lease Buyout";
    }

    public static final class ApplicationType {
        public static final String
            INDIVIDUAL          = "Individual",
            JOINT               = "Joint";
    }

    public static final class PageTitle {
        public static final String
            START_PAGE                      = "Start",
            PERSONAL_AND_RESIDENTIAL_PAGE   = "Personal and Residential",
            EMPLOYMENT_INFO_PAGE            = "Employment",
            LOAN_INFO_PAGE                  = "Loan/Finance",
            VEHICLE_INFO_PAGE               = "Vehicle",
            CO_APPLICANT_PERSONAL_INFO      = "Co-Applicant Personal Info",
            CO_APPLICANT_EMPLOYMENT_INFO    = "Co-Applicant Employment Info"
                        ;
    }

    public static final class APIKeys {
        public static final String
            APPLICATION_TYPE        = "application_type",
            COUNTRY                 = "country",
            LOAN_OR_FINANCE_TYPE    = "loan_or_finance_type",
            PERSONAL_FIRST_NAME              = "personal[first_name]",
            PERSONAL_LAST_NAME               = "personal[last_name]",
            PERSONAL_DATE_OF_BIRTH           = "personal[dob]",
            PERSONAL_SOCIAL_SECURITY_NUMBER  = "personal[social_security_num]",
            PERSONAL_PRIMARY_PHONE           = "personal[primary_phone]",
            PERSONAL_EMAIL_ADDRESS           = "personal[email]",
            PERSONAL_STREET_ADDRESS          = "personal[street_addr]",
            PERSONAL_CITY                    = "personal[city]",
            PERSONAL_STATE                   = "personal[state]",
            PERSONAL_ZIPCODE                 = "personal[zipcode]",
            PERSONAL_PRIMARY_APPLICANT       = "personal[is_primary_applicant]",
            PERSONAL_TIME_AT_RESIDENCE       = "personal[time_at_residence]",
            PERSONAL_STATUS_OF_RESIDENCE     = "personal[status_of_residence]",
            PERSONAL_MONTHLY_HOUSING_PAYMENT = "personal[monthly_housing_payment]",

            EMPLOYMENT_STATUS       = "employment[status]",
            OCCUPATION              = "employment[ocupation]",
            EMPLOYER                = "employment[employer]",
            WORK_PHONE              = "employment[work_phone]",
            TIME_AT_EMPLOYER        = "employment[time_at_employer]",
            GROSS_MONTHLY_INCOME    = "employment[gross_monthly_income]",

            VEHICLE_TYPE            = "vehicle[type]",
            VEHICLE_YEAR            = "vehicle[year]",
            VEHICLE_MAKE            = "vehicle[make]",
            VEHICLE_MODEL           = "vehicle[model]",
            VEHICLE_TRADING_IN_CURRENT_CAR = "vehicle[is_trading_in_current_car]",

            /*
            *LOAN TYPE --> NEW_OR_USED (DEALER)
            */
            LOF_TOTAL_SALES_PRICE   = "lof[total_sales_price]",
            LOF_REQUESTED_TERM      = "lof[requested_term]",
            LOF_CASH_DOWNPAYMENT    = "lof[cash_downpayment]",

            /*
            *LOAN TYPE --> REFINANCE AUTO
            */
            LOF_REMAINING_BALANCE   = "lof[remaining_balance]",
            LOF_INTEREST_RATE       = "lof[interest_rate]",
            LOF_MONTHLY_PAYMENT     = "lof[monthly_payment]",
            LOF_LIEN_HOLDER         = "lof[lien_holder]",
            LOF_NEXT_PAYMENT_DATE   = "lof[next_payment_date]",
            LOF_NEW_LOAN_TERM       = "lof[new_loan_term]",

            /*
             *LOAN TYPE --> AUTO LEASE BUYOUT
             */
            LOF_LEASE_BUYOUT_AMOUNT = "lof[lease_buyout_amount]",
            LOF_LEASE_EXPIRY_DATE   = "lof[lease_expiry_date]",
            LOF_LEASE_HOLDER        = "lof[lease_holder]",

            /*
            *LOAN TYPE --> PRIVATE PARTY (NON_DEALER)
            */
            LOF_LOAN_AMOUNT         = "lof[loan_amount]",
            LOF_TERM                = "lof[loan_term]",

            SELLER_FIRST_NAME       = "seller[first_name]",
            SELLER_LAST_NAME        = "seller[last_name]",
            SELLER_PHONE            = "seller[phone]",

            /*
            * APPLICATION TYPE --> JOINT
            */
            CO_APPLICANT_FIRST_NAME              = "co_applicant[first_name]",
            CO_APPLICANT_LAST_NAME               = "co_applicant[last_name]",
            CO_APPLICANT_DATE_OF_BIRTH           = "co_applicant[dob]",
            CO_APPLICANT_SOCIAL_SECURITY_NUMBER  = "co_applicant[social_security_num]",
            CO_APPLICANT_PRIMARY_PHONE           = "co_applicant[primary_phone]",
            CO_APPLICANT_EMAIL_ADDRESS           = "co_applicant[email]",
            CO_APPLICANT_STREET_ADDRESS          = "co_applicant[street_addr]",
            CO_APPLICANT_CITY                    = "co_applicant[city]",
            CO_APPLICANT_STATE                   = "co_applicant[state]",
            CO_APPLICANT_ZIPCODE                 = "co_applicant[zipcode]",
            CO_APPLICANT_PRIMARY_APPLICANT       = "co_applicant[is_primary_applicant]",
            CO_APPLICANT_TIME_AT_RESIDENCE       = "co_applicant[time_at_residence]",
            CO_APPLICANT_STATUS_OF_RESIDENCE     = "co_applicant[status_of_residence]",
            CO_APPLICANT_MONTHLY_HOUSING_PAYMENT = "co_applicant[monthly_housing_payment]",
            CO_APPLICANT_IS_ADDRESS_SAME_APPLICANT       = "co_applicant[is_address_same_as_applicant]",

            CO_APPLICANT_EMPLOYMENT_STATUS       = "co_applicant[emp][status]",
            CO_APPLICANT_OCCUPATION              = "co_applicant[emp][ocupation]",
            CO_APPLICANT_EMPLOYER                = "co_applicant[emp][employer]",
            CO_APPLICANT_WORK_PHONE              = "co_applicant[emp][work_phone]",
            CO_APPLICANT_TIME_AT_EMPLOYER        = "co_applicant[emp][time_at_employer]",
            CO_APPLICANT_GROSS_MONTHLY_INCOME    = "co_applicant[emp][gross_monthly_income]"
        ;
    }

        /*public static Map<String, String> insuranceKeyTitlePairs() {
            Map<String, String> insuranceKeyTitlePairs = new HashMap<>();
            insuranceKeyTitlePairs.put(HAVE_INSURANCE, "Do you already have an Insurance?");
            insuranceKeyTitlePairs.put(VEHICLE_YEAR, "Year");
            insuranceKeyTitlePairs.put(VEHICLE_MAKE, "Make");
            insuranceKeyTitlePairs.put(VEHICLE_MODEL, "Model");
            insuranceKeyTitlePairs.put(VEHICLE_OWNERSHIP, "Do you own or lease your vehicle?");
            insuranceKeyTitlePairs.put(VEHICLE_PRIMARY_USAGE, "What's your primary use for the vehicle?");
            insuranceKeyTitlePairs.put(VEHICLE_MILEAGE_COVERED_PER_YEAR, "How many miles do you drive each year?");
            insuranceKeyTitlePairs.put(VEHICLE_PRIMARY_PARKING, "Where do you primarily park this vehicle?");

            insuranceKeyTitlePairs.put(DRIVER_GENDER, "What's your gender?");
            insuranceKeyTitlePairs.put(DRIVER_MARITAL_STATUS, "What's your marital status?");
            insuranceKeyTitlePairs.put(DRIVER_CREDIT_SCORE, "What's your credit score?");
            insuranceKeyTitlePairs.put(DRIVER_HIGHEST_EDUCATION_LEVEL, "What's your highest level of education?");
            insuranceKeyTitlePairs.put(DRIVER_HOUSE_OWNERSHIP, "Do you own or rent your home?");
            insuranceKeyTitlePairs.put(DRIVER_INSURANCE_PERIOD, "How long have you been insured?");
            insuranceKeyTitlePairs.put(DRIVER_ACCIDENTS_TICKET_CLAIM, "Any accidents, or tickets, or claims in the past 3 years?");
            insuranceKeyTitlePairs.put(DRIVER_CURRENT_CAR_COMPANY, "What's your current car insurance company?");
            insuranceKeyTitlePairs.put(DRIVER_ANY_OTHER, "Do any of these apply to you?");

            return insuranceKeyTitlePairs;
        }*/



}
