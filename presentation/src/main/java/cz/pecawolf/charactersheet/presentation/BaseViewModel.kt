package cz.pecawolf.charactersheet.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import org.koin.java.KoinJavaComponent.inject

open class BaseViewModel : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val schedulerProvider: SchedulerProvider by inject(SchedulerProvider::class.java)

    fun <T> Single<T>.observe(
        onSuccess: (t: T) -> Unit = {},
        onError: (t: Throwable) -> Unit = {},
    ): Disposable {

        val observable = subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.mainThread())

        return observable
            .doOnSubscribe {
                compositeDisposable.add(it)
            }
            .subscribe(onSuccess, onError)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }

}
