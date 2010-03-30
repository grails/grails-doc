package org.grails.doc.filters

import org.radeox.filter.context.FilterContext
import org.radeox.filter.regex.RegexTokenFilter
import org.radeox.regex.MatchResult

class HeaderFilter extends RegexTokenFilter {

    HeaderFilter() {
        super(/(?m)^h(\d)\.\s+?(.*?)$/)
    }

    void handleMatch(StringBuffer out, MatchResult matchResult, FilterContext filterContext) {
        def header = matchResult.group(1)
        def content = matchResult.group(2)
        out << "<h$header>$content</h$header>"
    }
}
