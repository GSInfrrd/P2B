package infrrd.p2b.extractor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import infrrd.p2b.entity.ChequeDetails;
import infrrd.p2b.entity.DocumentDetails;
import infrrd.p2b.entity.FieldDetails;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChequeNumberExtractor implements DocumentDetailsExtractor {
	String findCheckwithKeywordRegex = "(Check)(\\W{1,4})(\\d{5,10})";
	String [] chequeNumberExtractionRegexList = {"\\d{5,}$", "(?<!\\d)[\\d\\s]{9,15}$(?!\\d)"};

	@Override
	public DocumentDetails extract(String ocrText, DocumentDetails docDetails) {
	//	log.info("Inside ChequeNumberExtractor class --> OCR text {}", ocrText);
		boolean checkNOFound = false;
		Pattern chequePattern = Pattern.compile(findCheckwithKeywordRegex);
		Matcher chequeMatcher = chequePattern.matcher(ocrText);
		if(chequeMatcher.find()){
			String chequeNO = chequeMatcher.group(3);
			docDetails.setCheckNumber(chequeNO);
			checkNOFound = true;
		}
		if (!checkNOFound) {
			String[] ocrTextArray = ocrText.split("\\n");
			for (String chequeNumberExtractionRegex : chequeNumberExtractionRegexList) {
				int count = 0;
				for (String lineText : ocrTextArray) {
					count++;
					lineText = lineText.replaceAll("[;.,|\\s]$", "").trim();
					chequePattern = Pattern.compile(chequeNumberExtractionRegex);
					chequeMatcher = chequePattern.matcher(lineText);
					if (chequeMatcher.find()) {
						String chequeNO = chequeMatcher.group().replaceAll(" ", "");
						docDetails.setCheckNumber(chequeNO);
						checkNOFound = true;
						log.info("Cheque Number Found  --> OCR text {}", chequeNO);
					}
					if (checkNOFound || count > 10) {
						break;
					}
				}
				if (checkNOFound) {
					break;
				}
			}
		}
		

		return docDetails;
	}

}
