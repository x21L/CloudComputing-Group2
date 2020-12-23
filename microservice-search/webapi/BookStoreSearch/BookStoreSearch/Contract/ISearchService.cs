using System.Collections.Generic;
using System.Threading.Tasks;
using BookStoreSearch.Entity;

namespace BookStoreSearch.Contract
{
    public interface ISearchService<T> where T : IStoreable
    {
        /// <summary>
        /// Performs a search request.
        /// </summary>
        /// <param name="query">Search query.</param>
        /// <param name="settings"><see cref="SearchSettings"/> settings.</param>
        /// <returns>List of search results.</returns>
        Task<List<T>> Search(string query, SearchSettings settings);

        /// <summary>
        /// Adds or updates an <see cref="IStoreable"/> in the search database depending on if the id is set or not.
        /// </summary>
        /// <param name="item"><see cref="IStoreable"/></param>
        /// <returns><see cref="Task"/></returns>
        Task<T> AddOrUpdate(T item);

        /// <summary>
        /// Deletes the item with the given id.
        /// </summary>
        /// <param name="id">Id of the item.</param>
        /// <returns><see cref="Task"/></returns>
        Task Delete(string id);

        /// <summary>
        /// Gets the item with the given id.
        /// </summary>
        /// <param name="id">Id of the item.</param>
        /// <returns><see cref="Task"/></returns>
        Task<T> Get(string id);
    }
}
