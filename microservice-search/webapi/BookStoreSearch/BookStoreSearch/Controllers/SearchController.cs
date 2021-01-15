using System;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using BookStoreSearch.Contract;
using BookStoreSearch.Entity;
using Microsoft.AspNetCore.Mvc;

namespace BookStoreSearch.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class SearchController : ControllerBase
    {
        private readonly ISearchService<Book> _searchService;

        public SearchController(ISearchService<Book> searchService)
        {
            _searchService = searchService;
        }

        /// <summary>
        /// Gets the result of the search query.
        /// </summary>
        /// <param name="query">Free text search query.</param>
        /// <param name="from">Optional paging parameter from to start returning the objects from an index.</param>
        /// <param name="size">Optional paging parameter size to limit the amount of returned objects.</param>
        /// <returns>200 OK with search results as payload.</returns>
        [HttpGet]
        public async Task<ActionResult> Get([FromQuery(Name = "query")] string query, [FromQuery(Name = "from")] int from = 0, [FromQuery(Name = "size")] int size = 10)
        {
            await Task.Run(() => true);
            return Ok("Hello");
        }
    }
}
