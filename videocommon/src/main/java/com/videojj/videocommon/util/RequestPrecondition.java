package com.videojj.videocommon.util;

import com.videojj.videocommon.exception.BadRequestException;

public class RequestPrecondition {

  private static String CONTAIN_EMPTY_ARGUMENT = "request payload should not be contain empty.";

  private static String ILLEGAL_MODEL = "request model is invalid";

  private static String ILLEGAL_NUMBER = "number should be positive";


  public static void checkArgumentsNotEmpty(String... args) {
    checkArguments(!isContainEmpty(args), CONTAIN_EMPTY_ARGUMENT);
  }

  private static boolean isContainEmpty(String[] args) {

    if (args == null){
      return false;
    }

    for (String arg: args){
      if (arg == null || "".equals(arg)){
        return true;
      }
    }

    return false;
  }

  public static void checkModel(boolean valid){
    checkArguments(valid, ILLEGAL_MODEL);
  }

  public static void checkArguments(boolean expression, Object errorMessage) {
    if (!expression) {
      throw new BadRequestException(String.valueOf(errorMessage));
    }
  }

  public static void checkNumberPositive(int... args){
    for (int num: args){
      if (num <= 0){
        throw new BadRequestException(ILLEGAL_NUMBER);
      }
    }
  }

  public static void checkNumberPositive(long... args){
    for (long num: args){
      if (num <= 0){
        throw new BadRequestException(ILLEGAL_NUMBER);
      }
    }
  }

  public static void checkNumberNotNegative(int... args){
    for (int num: args){
      if (num < 0){
        throw new BadRequestException(ILLEGAL_NUMBER);
      }
    }
  }



}
