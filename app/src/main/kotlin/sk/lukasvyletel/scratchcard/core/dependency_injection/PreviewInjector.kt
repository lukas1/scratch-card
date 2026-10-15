package sk.lukasvyletel.scratchcard.core.dependency_injection

import sk.lukasvyletel.scratchcard.core.annotations.ExcludeFromCoverage

@ExcludeFromCoverage
class PreviewInjector : Injector {
    override val globalModule: GlobalModule = GlobalModule()
}
