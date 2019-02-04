package com.wix.e2e.http.matchers.internal

import com.wix.e2e.http.api.Marshaller.Implicits.marshaller
import com.wix.e2e.http.matchers.ResponseMatchers._
import com.wix.e2e.http.matchers.drivers.HttpResponseFactory._
import com.wix.e2e.http.matchers.drivers.HttpResponseMatchers._
import com.wix.e2e.http.matchers.drivers.{HttpMessageTestSupport, MatchersTestSupport}
import org.specs2.mutable.Spec
import org.specs2.specification.Scope


class ResponseBodyAndStatusMatchersTest extends Spec with MatchersTestSupport {

  trait ctx extends Scope with HttpMessageTestSupport


  "ResponseBodyAndStatusMatchers" should {

    "match successful response with body content" in new ctx {
      aSuccessfulResponseWith(content) must beSuccessfulWith(content)
      aSuccessfulResponseWith(content) must not( beSuccessfulWith(anotherContent) )
    }

    "provide a proper message to user sent a matcher to an entity matcher" in new ctx {
      failureMessageFor(beSuccessfulWith(entity = be_===(content)), matchedOn = aResponseWith(content)) must_===
        s"Matcher misuse: `beSuccessfulWith` received a matcher to match against, please use `beSuccessfulWithEntityThat` instead."
    }

    "match successful response with body content matcher" in new ctx {
      aSuccessfulResponseWith(content) must beSuccessfulWithBodyThat(must = be_===( content ))
      aSuccessfulResponseWith(content) must not( beSuccessfulWithBodyThat(must = be_===( anotherContent )) )
    }

    "match invalid response with body content" in new ctx {
      anInvalidResponseWith(content) must beInvalidWith(content)
      anInvalidResponseWith(content) must not( beInvalidWith(anotherContent) )
    }

    "match invalid response with body content matcher" in new ctx {
      anInvalidResponseWith(content) must beInvalidWithBodyThat(must = be_===( content ))
      anInvalidResponseWith(content) must not( beInvalidWithBodyThat(must = be_===( anotherContent )) )
    }

    "match successful response with binary body content" in new ctx {
      aSuccessfulResponseWith(binaryContent) must beSuccessfulWith(binaryContent)
      aSuccessfulResponseWith(binaryContent) must not( beSuccessfulWith(anotherBinaryContent) )
    }

    "match successful response with binary body content matcher" in new ctx {
      aSuccessfulResponseWith(binaryContent) must beSuccessfulWithBodyDataThat(must = be_===( binaryContent ))
      aSuccessfulResponseWith(binaryContent) must not( beSuccessfulWithBodyDataThat(must = be_===( anotherBinaryContent )) )
    }

    "match successful response with entity" in new ctx {
      aSuccessfulResponseWith(marshaller.marshall(someObject)) must beSuccessfulWith( someObject )
      aSuccessfulResponseWith(marshaller.marshall(someObject)) must not( beSuccessfulWith( anotherObject ) )
    }

    "match successful response with entity with custom marshaller" in new ctx {
      aSuccessfulResponseWith(marshaller.marshall(someObject)) must beSuccessfulWith( someObject )
      aSuccessfulResponseWith(marshaller.marshall(someObject)) must not( beSuccessfulWith( anotherObject ) )
    }

    "match successful response with entity matcher" in new ctx {
      aSuccessfulResponseWith(marshaller.marshall(someObject)) must beSuccessfulWithEntityThat( must = be_===( someObject ) )
      aSuccessfulResponseWith(marshaller.marshall(someObject)) must not( beSuccessfulWithEntityThat( must = be_===( anotherObject ) ) )
    }

    "match successful response with headers" in new ctx {
      aSuccessfulResponseWith(header, anotherHeader) must beSuccessfulWithHeaders(header, anotherHeader)
      aSuccessfulResponseWith(header) must not( beSuccessfulWithHeaders(anotherHeader) )
    }

    "match successful response with header matcher" in new ctx {
      aSuccessfulResponseWith(header) must beSuccessfulWithHeaderThat(must = be_===(header._2), withHeaderName = header._1)
      aSuccessfulResponseWith(header) must not( beSuccessfulWithHeaderThat(must = be_===(anotherHeader._2), withHeaderName = header._1) )
    }

    "match successful response with cookies" in new ctx {
      aSuccessfulResponseWithCookies(cookie, anotherCookie) must beSuccessfulWithCookie(cookie.name)
      aSuccessfulResponseWithCookies(cookie) must not( beSuccessfulWithCookie(anotherCookie.name) )
    }

    "match successful response with cookie matcher" in new ctx {
      aSuccessfulResponseWithCookies(cookie) must beSuccessfulWithCookieThat(must = cookieWith(cookie.value))
      aSuccessfulResponseWithCookies(cookie) must not( beSuccessfulWithCookieThat(must = cookieWith(anotherCookie.value)) )
    }

    "provide a proper message to user sent a matcher to an entity matcher" in new ctx {
      failureMessageFor(haveBodyWith(entity = be_===(someObject)), matchedOn = aResponseWith(content)) must_===
        s"Matcher misuse: `haveBodyWith` received a matcher to match against, please use `haveBodyWithEntityThat` instead."
    }
  }
}
