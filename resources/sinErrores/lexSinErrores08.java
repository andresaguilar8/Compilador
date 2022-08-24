String getErrorCode(String testCaseFilePath)  {

        String lineWithTheCode = null;
        try {
        lineWithTheCode = (new BufferedReader(new FileReader(testCaseFilePath))).readLine();
        } catch (IOException e) {
        e.printStackTrace();
        }

        String errorCode = lineWithTheCode.substring(3);
        return errorCode;
}