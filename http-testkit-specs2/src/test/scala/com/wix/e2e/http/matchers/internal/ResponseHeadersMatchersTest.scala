package com.wix.e2e.http.matchers.internal

import com.wix.e2e.http.matchers.ResponseMatchers._
import com.wix.e2e.http.matchers.drivers.HttpResponseFactory._
import com.wix.e2e.http.matchers.drivers.{HttpMessageTestSupport, MatchersTestSupport}
import org.specs2.matcher.AlwaysMatcher
import org.specs2.mutable.Spec
import org.specs2.specification.Scope


class ResponseHeadersMatchersTest extends Spec with MatchersTestSupport {

  trait ctx extends Scope with HttpMessageTestSupport

  "ResponseHeadersMatchers" should {

    "contain header will check if any header is present" in new ctx {
      aResponseWithHeaders(header, anotherHeader) must haveAnyHeadersOf(header)
    }

    "return detailed message on hasAnyOf match failure" in new ctx {
      failureMessageFor(haveAnyHeadersOf(header, anotherHeader), matchedOn = aResponseWithHeaders(yetAnotherHeader, andAnotherHeader)) must_===
        s"Could not find header [${header._1}, ${anotherHeader._1}] but found those: [${yetAnotherHeader._1}, ${andAnotherHeader._1}]"
    }

    "contain header will check if all headers are present" in new ctx {
      aResponseWithHeaders(header, anotherHeader, yetAnotherHeader) must haveAllHeadersOf(header, anotherHeader)
    }

    "allOf matcher will return a message stating what was found, and what is missing from header list" in new ctx {
      failureMessageFor(haveAllHeadersOf(header, anotherHeader), matchedOn = aResponseWithHeaders(yetAnotherHeader, header)) must_===
        s"Could not find header [${anotherHeader._1}] but found those: [${header._1}]."
    }

    "same header as will check if the same headers is present" in new ctx {
      aResponseWithHeaders(header, anotherHeader) must haveTheSameHeadersAs(header, anotherHeader)
      aResponseWithHeaders(header, anotherHeader) must not( haveTheSameHeadersAs(header) )
      aResponseWithHeaders(header) must not( haveTheSameHeadersAs(header, anotherHeader) )
    }

    "haveTheSameHeadersAs matcher will return a message stating what was found, and what is missing from header list" in new ctx {
      failureMessageFor(haveTheSameHeadersAs(header, anotherHeader), matchedOn = aResponseWithHeaders(yetAnotherHeader, header)) must_===
        s"Response header is not identical, missing headers from response: [${anotherHeader._1}], response contained extra headers: [${yetAnotherHeader._1}]."
    }

    "header name compare should be case insensitive" in new ctx {
      aResponseWithHeaders(header) must haveAnyHeadersOf(header.copy(_1 = header._1.toUpperCase))
      aResponseWithHeaders(header) must not( haveAnyHeadersOf(header.copy(_2 = header._2.toUpperCase)) )

      aResponseWithHeaders(header) must haveAllHeadersOf(header.copy(_1 = header._1.toUpperCase))
      aResponseWithHeaders(header) must not( haveAllHeadersOf(header.copy(_2 = header._2.toUpperCase)) )

      aResponseWithHeaders(header) must haveTheSameHeadersAs(header.copy(_1 = header._1.toUpperCase))
      aResponseWithHeaders(header) must not( haveTheSameHeadersAs(header.copy(_2 = header._2.toUpperCase)) )
    }
    
    "response with no headers will show a 'no headers' message" in new ctx {
      failureMessageFor(haveAnyHeadersOf(header), matchedOn = aResponseWithNoHeaders ) must_===
        "Response did not contain any headers."

      failureMessageFor(haveAllHeadersOf(header), matchedOn = aResponseWithNoHeaders ) must_===
        "Response did not contain any headers."

      failureMessageFor(haveTheSameHeadersAs(header), matchedOn = aResponseWithNoHeaders ) must_===
        "Response did not contain any headers."
    }

    "ignore cookies and set cookies from headers comparison" in new ctx {
      aResponseWithCookies(cookie) must not( haveAnyHeadersOf("Set-Cookie" -> s"${cookie.name}=${cookie.value}") )
      aResponseWithCookies(cookie) must not( haveAllHeadersOf("Set-Cookie" -> s"${cookie.name}=${cookie.value}") )
      aResponseWithCookies(cookie) must not( haveTheSameHeadersAs("Set-Cookie" -> s"${cookie.name}=${cookie.value}") )
    }

    "match if any header satisfy the composed matcher" in new ctx {
      aResponseWithHeaders(header) must haveAnyHeaderThat(must = be_===(header._2), withHeaderName = header._1)
      aResponseWithHeaders(header) must not( haveAnyHeaderThat(must = be_===(anotherHeader._2), withHeaderName = header._1) )
    }

    "return informative error messages" in new ctx {
      failureMessageFor(haveAnyHeaderThat(must = AlwaysMatcher(), withHeaderName = nonExistingHeaderName), matchedOn = aResponseWithHeaders(header)) must_===
        s"Response contain header names: [${header._1}] which did not contain: [$nonExistingHeaderName]"
      failureMessageFor(haveAnyHeaderThat(must = AlwaysMatcher(), withHeaderName = nonExistingHeaderName), matchedOn = aResponseWithNoHeaders) must_===
        "Response did not contain any headers."
      failureMessageFor(haveAnyHeaderThat(must = be_===(anotherHeader._2), withHeaderName = header._1), matchedOn = aResponseWithHeaders(header)) must_===
        s"Response header [${header._1}], did not match { ${be_===(anotherHeader._2).apply(header._2).message} }"
    }
  }
}