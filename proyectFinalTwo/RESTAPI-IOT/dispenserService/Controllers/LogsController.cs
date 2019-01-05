using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using dispenserService.Models;
using dispenserService.Util;

namespace dispenserService.Controllers
{
    public class LogsController : ApiController
    {
        private dispenserServiceContext db = new dispenserServiceContext();

        // GET: api/Logs
        [Route("api/Logs")]
        [HttpGet]
        public IQueryable<Log> GetLogs()
        {
            return db.Logs;
        }

        // GET: api/Logs/5
        [Route("api/Logs/{id}", Name = "GetLogById")]
        [HttpGet]
        [ResponseType(typeof(Log))]
        public async Task<IHttpActionResult> GetLog(int id)
        {
            Log log = await db.Logs.FindAsync(id);
            if (log == null)
            {
                return NotFound();
            }

            return Ok(log);
        }

        // PUT: api/Logs/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutLog(int id, Log log)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != log.LogId)
            {
                return BadRequest();
            }

            db.Entry(log).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!LogExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        /*
        // POST: api/Logs
        [ResponseType(typeof(Log))]
        public async Task<IHttpActionResult> PostLog(Log log)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            DateTime _time = DateTime.Now;
            log.CurrentDateTime = _time;
            db.Logs.Add(log);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = log.LogId }, log);
        }*/

        // POST: api/Logs
        [Route("api/Logs")]
        [HttpPost]
        [ResponseType(typeof(Log))]
        public async Task<IHttpActionResult> SaveLog(DispenserForm dispensor)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            DateTime _time = DateTime.Now;

            Log _log = new Log();
            _log.ThingId = dispensor.ThingId;
            _log.ActionType = dispensor.ActionType;
            _log.AmountDailyFood = dispensor.AmountDailyFood;
            _log.FoodPortion = dispensor.FoodPortion;
            _log.MilliLiterWater = dispensor.MilliLiterWater;
            _log.MinPercentWater = dispensor.MinPercentWater;
            _log.CurrentAmountFood = dispensor.CurrentAmountFood;
            _log.AmountFoodDownloaded = dispensor.AmountFoodDownloaded;
            _log.CurrentMilliLiterWater = dispensor.CurrentMilliLiterWater;
            _log.MilliLiterWaterDownloaded = dispensor.MilliLiterWaterDownloaded;
            _log.AngleServoFood = dispensor.AngleServoFood;
            _log.OpeningSecondsFood = dispensor.OpeningSecondsFood;
            _log.AngleServoWater = dispensor.AngleServoWater;
            _log.OpeningSecondsWater = dispensor.OpeningSecondsWater;
            _log.CurrentDateTime = _time;

            db.Logs.Add(_log);
            await db.SaveChangesAsync();

            if (dispensor.DispenseFood == 1)
            {
                FoodDispenser _foodDispenser = new FoodDispenser();
                _foodDispenser.ThingId = dispensor.ThingId;
                _foodDispenser.ActionType = dispensor.ActionType;
                _foodDispenser.AmountDailyFood = dispensor.AmountDailyFood;
                _foodDispenser.FoodPortion = dispensor.FoodPortion;
                _foodDispenser.CurrentAmountFood = dispensor.CurrentAmountFood;
                _foodDispenser.AmountFoodDownloaded = dispensor.AmountFoodDownloaded;
                _foodDispenser.AngleServoFood = dispensor.AngleServoFood;
                _foodDispenser.OpeningSecondsFood = dispensor.OpeningSecondsFood;
                _foodDispenser.CurrentDateTime = _time;

                db.FoodDispensers.Add(_foodDispenser);
                await db.SaveChangesAsync();
            }

            if (dispensor.DispenseWater == 1)
            {
                WaterDispenser _waterDispenser = new WaterDispenser();
                _waterDispenser.ThingId = dispensor.ThingId;
                _waterDispenser.ActionType = dispensor.ActionType;
                _waterDispenser.MilliLiterWater = dispensor.MilliLiterWater;
                _waterDispenser.MinPercentWater = dispensor.MinPercentWater;
                _waterDispenser.CurrentMilliLiterWater = dispensor.CurrentMilliLiterWater;
                _waterDispenser.MilliLiterWaterDownloaded = dispensor.MilliLiterWaterDownloaded;
                _waterDispenser.AngleServoWater = dispensor.AngleServoWater;
                _waterDispenser.OpeningSecondsWater = dispensor.OpeningSecondsWater;
                _waterDispenser.CurrentDateTime = _time;

                db.WaterDispensers.Add(_waterDispenser);
                await db.SaveChangesAsync();
            }

            return CreatedAtRoute("GetLogById", new { id = _log.LogId }, _log);
        }

        // DELETE: api/Logs/5
        [ResponseType(typeof(Log))]
        public async Task<IHttpActionResult> DeleteLog(int id)
        {
            Log log = await db.Logs.FindAsync(id);
            if (log == null)
            {
                return NotFound();
            }

            db.Logs.Remove(log);
            await db.SaveChangesAsync();

            return Ok(log);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool LogExists(int id)
        {
            return db.Logs.Count(e => e.LogId == id) > 0;
        }
    }
}