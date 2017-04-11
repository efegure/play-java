import play.mvc.EssentialFilter;
import play.filters.hosts.AllowedHostsFilter;
import play.http.HttpFilters;
import play.filters.cors.CORSFilter;
import javax.inject.Inject;

public class Filter implements HttpFilters {

    @Inject
    AllowedHostsFilter allowedHostsFilter;

    public EssentialFilter[] filters() {
        return new EssentialFilter[] { allowedHostsFilter.asJava() };
    }
    @Inject
    CORSFilter corsFilter;

    public EssentialFilter[] Corsfilters() {
        return new EssentialFilter[] { corsFilter.asJava() };
    }
}