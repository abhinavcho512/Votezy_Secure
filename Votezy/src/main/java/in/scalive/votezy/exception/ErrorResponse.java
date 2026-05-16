package in.scalive.votezy.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {  // agr alg error aye to , customarised error response that return 2 thing sC and errorMessage
	private int statusCode;
	private String messageString;

}
